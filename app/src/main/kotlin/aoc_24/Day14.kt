package aoc_24

class Day14 {

    companion object {
        private const val RESOURCE_BASENAME = "day14"
        private const val RESOURCE_TEST = "${RESOURCE_BASENAME}_test.txt"
        private const val RESOURCE_INPUT = "${RESOURCE_BASENAME}_input.txt"
    }

    fun part1() {
        println("=== Part 1 ===")
        val testInput = getResourceAsStringCollection(RESOURCE_TEST)
        val testResult = getFactor(robots = testInput.toRobots(), width = 11, height = 7)
        println("test result - $testResult")
        if (testResult != 12) throw Exception("example 1 not passing")

        val input = getResourceAsStringCollection(RESOURCE_INPUT)
        val result = getFactor(robots = input.toRobots(), width = 101, height = 103)
        println("result - $result")
    }

    fun part2() {
        println("=== Part 2 ===")
        val input = getResourceAsStringCollection(RESOURCE_INPUT)
        val result = getEasterEgg(robots = input.toRobots())
        println("result - $result")
    }

    private fun List<String>.toRobots(): List<Robot> = map { line ->
        Robot(
            posX = line.substringAfter("p=").takeWhile { it != ',' }.toInt(),
            posY = line.split(" ")[0].substringAfter(",").toInt(),
            velX = line.substringAfter("v=").takeWhile { it != ',' }.toInt(),
            velY = line.split(" ")[1].substringAfter(",").toInt(),
        )
    }

    private fun getFactor(robots: List<Robot>, width: Int, height: Int): Int {
        val halfW = width / 2
        val halfH = height / 2

        val locations = robots.map { r ->
            val x = (r.posX + 100 * r.velX).mod(width)
            val y = (r.posY + 100 * r.velY).mod(height)
            x to y
        }

        fun Pair<Int,Int>.getQ(): Int {
            return when {
                first < halfW && second < halfH -> 1
                first > halfW && second < halfH -> 2
                first > halfW && second > halfH -> 3
                else -> 4
            }
        }

        return locations.filterNot { it.first == halfW || it.second == halfH }
            .groupingBy { it.getQ() }
            .eachCount()
            .values
            .reduce(Int::times)
    }

    private fun getEasterEgg(robots: List<Robot>): Int {
        val maxIterations = 101 * 103

        (0..maxIterations).forEach { i ->
            val locations = robots.map { r ->
                val x = (r.posX + i * r.velX).mod(101)
                val y = (r.posY + i * r.velY).mod(103)
                x to y
            }

            // find long line of robots next to each other
            locations.groupBy { it.second }
                .filter { it.value.size > 10 }
                .onEach { row ->
                    val cols = row.value.map { it.first }
                    val line = (0..cols.max()).joinToString(separator = "") {
                        if (cols.contains(it)) "X" else "_"
                    }

                    // if there is 10 robots in line, it's our case
                    if (line.contains("XXXXXXXXXX")) return i
                }
        }

        return 0
    }

    data class Robot(
        val posX: Int,
        val posY: Int,
        val velX: Int,
        val velY: Int,
    )
}

fun main() {
    Day14().apply {
        part1()
        part2()
    }
}
