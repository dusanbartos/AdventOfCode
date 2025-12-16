package aoc_24

class Day13 {

    companion object {
        private const val RESOURCE_BASENAME = "day13"
        private const val RESOURCE_TEST = "${RESOURCE_BASENAME}_test.txt"
        private const val RESOURCE_INPUT = "${RESOURCE_BASENAME}_input.txt"
        private const val MAX_PRESS_COUNT = 100
        private const val COST_A = 3
        private const val COST_B = 1
    }

    fun part1() {
        println("=== Part 1 ===")
        val testInput = getResourceAsStringCollection(RESOURCE_TEST)
        val testResult = testInput.toMachines().sumOf { it.getCheapestCost() ?: 0 }
        println("test result - $testResult")
        if (testResult != 480L) throw Exception("example 1 not passing")

        val input = getResourceAsStringCollection(RESOURCE_INPUT)
        val result = input.toMachines().sumOf { it.getCheapestCost() ?: 0 }
        println("result - $result")
    }

    fun part2() {
        println("=== Part 2 ===")
        val input = getResourceAsStringCollection(RESOURCE_INPUT)
        val result = input.toMachines()
            .map { it.copy(prize = it.prize.first + 10_000_000_000_000 to it.prize.second + 10_000_000_000_000) }
            .sumOf { it.tokens() }
        println("result - $result")
    }

    private fun List<String>.toMachines(prizeOffset: Long = 0L): List<Machine> {
        return this.windowed(3, 4) { win ->
            val a = win[0]
            val b = win[1]
            val p = win[2]

            Machine(
                btnA = a.toXDir() to a.toYDir(),
                btnB = b.toXDir() to b.toYDir(),
                prize = prizeOffset + p.toX() to prizeOffset + p.toY(),
            )
        }
    }

    private fun String.toX(): Int = substringAfter("X=").substringBefore(",").toInt()
    private fun String.toXDir(): Int = substringAfter("X+").substringBefore(",").toInt()
    private fun String.toY(): Int = substringAfter("Y=").toInt()
    private fun String.toYDir(): Int = substringAfter("Y+").toInt()

    data class Machine(
        val btnA: Pair<Int, Int>,
        val btnB: Pair<Int, Int>,
        val prize: Pair<Long, Long>,
    ) {
        fun getCheapestCost(): Long? =
            getWays().minOfOrNull { (a, b) -> COST_A * a + COST_B * b }

        private fun getWays(): List<Pair<Int, Long>> {
            val result = mutableListOf<Pair<Int, Long>>()

            var i = 0
            while (i < MAX_PRESS_COUNT) {
                if (i * btnA.first > prize.first || i * btnA.second > prize.second) {
                    break
                }

                if ((prize.first - i * btnA.first) % btnB.first == 0L &&
                    (prize.second - i * btnA.second) % btnB.second == 0L) {
                    val iBX = (prize.first - i * btnA.first) / btnB.first
                    val iBY = (prize.second - i * btnA.second) / btnB.second
                    if (iBX == iBY) {
                        result.add(i to iBX)
                    }
                }
                i++
            }

            return result
        }

        fun tokens(): Long {
            val a = (prize.first * btnB.second - prize.second * btnB.first) / (btnA.first * btnB.second - btnA.second * btnB.first)
            val b = (prize.first - btnA.first * a) / btnB.first
            return if (a * btnA.first + b * btnB.first == prize.first && a * btnA.second + b * btnB.second == prize.second) COST_A * a + COST_B * b else 0
        }
    }
}

fun main() {
    Day13().apply {
        part1()
        part2()
    }
}
