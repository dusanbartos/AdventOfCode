package aoc_24

class Day07 {

    companion object {
        private const val RESOURCE_BASENAME = "day07"
        private const val RESOURCE_TEST = "${RESOURCE_BASENAME}_test.txt"
        private const val RESOURCE_INPUT = "${RESOURCE_BASENAME}_input.txt"
    }

    fun part1() {
        println("=== Part 1 ===")
        val testInput = getResourceAsStringCollection(RESOURCE_TEST)
        val testResult = getPart1(testInput)
        println("test result - $testResult")
        if (testResult != 3749L) throw Exception("example 1 not passing")

        val input = getResourceAsStringCollection(RESOURCE_INPUT)
        val result = getPart1(input)
        println("result - $result")
    }

    fun part2() {
        println("=== Part 2 ===")
        val testInput = getResourceAsStringCollection(RESOURCE_TEST)
        val testResult = getPart2(testInput)
        println("test result - $testResult")
        if (testResult != 11387L) throw Exception("example 2 not passing")

        val input = getResourceAsStringCollection(RESOURCE_INPUT)
        val result = getPart2(input)
        println("result - $result")
    }

    private fun getPart1(input: List<String>): Long {
        val equations = getEquations(input)
        return equations.filter { it.test() }.sumOf { it.result }
    }

    private fun getPart2(input: List<String>): Long {
        val equations = getEquations(input)
        return equations.filter { it.test2() }.sumOf { it.result }
    }

    private fun getEquations(lines: List<String>): List<Equation> {
        return lines.map { line ->
            Equation(
                result = line.substringBefore(":").toLong(),
                numbers = line.substringAfter(":").split("\\s+".toRegex()).mapNotNull { it.toIntOrNull() }
            )
        }
    }

    data class Equation(
        val result: Long,
        val numbers: List<Int>,
    ) {
        fun test(): Boolean {
            val results = mutableListOf<Long>()
            numbers.forEachIndexed { index, num ->
                if (index == 0) results.add(num.toLong())
                else {
                    val tempRes = results.toList()
                    tempRes.forEachIndexed { ri, r ->
                        results[ri] = r + num
                        results.add(r * num)
                    }
                }
            }
            return results.any { it == result }
        }

        fun test2(): Boolean {
            val results = mutableListOf<Long>()
            numbers.forEachIndexed { index, num ->
                if (index == 0) results.add(num.toLong())
                else {
                    val tempRes = results.toList()
                    tempRes.forEachIndexed { ri, r ->
                        results[ri] = r + num
                        results.add(r * num)
                        results.add("$r$num".toLong())
                    }
                }
            }
            return results.any { it == result }
        }
    }
}

fun main() {
    Day07().apply {
        part1()
        part2()
    }
}
