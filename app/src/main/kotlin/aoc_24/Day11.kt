package aoc_24

class Day11 {

    companion object {
        private const val RESOURCE_BASENAME = "day11"
        private const val RESOURCE_TEST = "${RESOURCE_BASENAME}_test.txt"
        private const val RESOURCE_INPUT = "${RESOURCE_BASENAME}_input.txt"
    }

    fun part1() {
        println("=== Part 1 ===")
        val testInput = getResourceAsStringCollection(RESOURCE_TEST)
        val stones = testInput.first().splitToDigits()

        val testResult1 = stones.countStonesAfter(6)
        println("test result 1.1 - $testResult1")
        if (testResult1 != 22L) throw Exception("example 1.1 not passing")

        val testResult2 = stones.countStonesAfter(25)
        println("test result 1.2 - $testResult2")
        if (testResult2 != 55312L) throw Exception("example 1.2 not passing")

        val input = getResourceAsStringCollection(RESOURCE_INPUT)
        val result = input.first().splitToDigits().countStonesAfter(25)
        println("result - $result")
    }

    fun part2() {
        println("=== Part 2 ===")
        val input = getResourceAsStringCollection(RESOURCE_INPUT)
        val result = input.first().splitToDigits().countStonesAfter(75)
        println("result - $result")
    }

    private fun List<Int>.countStonesAfter(blinkCount: Int): Long {
        val initial = this.map { it.toLong() }.groupingBy { it }.eachCount().mapValues { it.value.toLong() }
        val m = (0..<blinkCount).fold(initial) { map, i ->
            val tempMap = mutableMapOf<Long, Long>()
            map.entries.forEach { (num, count) ->
                if (num == 0L) {
                    tempMap.merge(1, count, Long::plus)
                } else {
                    val str = "$num"
                    if (str.length % 2 == 0) {
                        tempMap.merge(str.take(str.length / 2).toLong(), count, Long::plus)
                        tempMap.merge(str.takeLast(str.length / 2).toLong(), count, Long::plus)
                    } else {
                        tempMap.merge(num * 2024, count, Long::plus)
                    }
                }
            }
            tempMap
        }
        return m.values.sum()
    }
}

fun main() {
    Day11().apply {
        part1()
        part2()
    }
}
