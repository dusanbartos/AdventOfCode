package aoc_24

class Day08 {

    companion object {
        private const val RESOURCE_BASENAME = "day08"
        private const val RESOURCE_TEST = "${RESOURCE_BASENAME}_test.txt"
        private const val RESOURCE_INPUT = "${RESOURCE_BASENAME}_input.txt"
    }

    fun part1() {
        println("=== Part 1 ===")
        val testInput = getResourceAsStringCollection(RESOURCE_TEST)
        val testResult = getPart1(testInput)
        println("test result - $testResult")
        if (testResult != 14) throw Exception("example 1 not passing")

        val input = getResourceAsStringCollection(RESOURCE_INPUT)
        val result = getPart1(input)
        println("result - $result")
    }

    fun part2() {
        println("=== Part 2 ===")
        val testInput = getResourceAsStringCollection(RESOURCE_TEST)
        val testResult = getPart2(testInput)
        println("test result - $testResult")
        if (testResult != 34) throw Exception("example 2 not passing")

        val input = getResourceAsStringCollection(RESOURCE_INPUT)
        val result = getPart2(input)
        println("result - $result")
    }

    private fun getPart1(input: List<String>): Int {
        val height = input.size
        val width = input.first().length

        val antennas = input.getAntennas()
        val antinodes = antennas.getAntinodes().filter { it.isInside(height, width) }.toSet()
        return antinodes.size
    }

    private fun getPart2(input: List<String>): Int {
        val height = input.size
        val width = input.first().length

        val antennas = input.getAntennas()
        val antinodes = antennas.getAntinodes(maxH = height, maxW = width).filter { it.isInside(height, width) }.toSet()
        return antinodes.size
    }

    private fun List<String>.getAntennas(): List<Antenna> {
        val regex = "[a-zA-Z0-9]".toRegex()
        return this.flatMapIndexed { index, line ->
            regex.findAll(line).map { match ->
                Antenna(
                    frequency = match.value,
                    location = Coordinate(row = index, col = match.range.first)
                )
            }
        }
    }

    private fun List<Antenna>.getAntinodes(): List<Coordinate> {
        val result = mutableListOf<Coordinate>()
        this.groupBy { it.frequency }.map { it.value }.forEach { grp ->
            grp.map { i -> grp.map { i to it } }
                .flatten()
                .filter { (left, right) -> left != right }
                .onEach { (i, j) -> result.addAll(i.getAntinodesWith(j)) }
        }
        return result
    }

    private fun List<Antenna>.getAntinodes(maxH: Int, maxW: Int): List<Coordinate> {
        val result = mutableListOf<Coordinate>()
        this.groupBy { it.frequency }.map { it.value }.forEach { grp ->
            grp.map { i -> grp.map { i to it } }
                .flatten()
                .filter { (left, right) -> left != right }
                .onEach { (i, j) -> result.addAll(i.getAntinodesWith(j, maxH, maxW)) }
        }
        return result
    }

    data class Antenna(
        val frequency: String,
        val location: Coordinate,
    ) {
        val r get() = location.row
        val c get() = location.col

        fun getAntinodesWith(other: Antenna): List<Coordinate> {
            return listOf(
                Coordinate(row = 2 * other.r - r, col = 2 * other.c - c),
                Coordinate(row = 2 * r - other.r, col = 2 * c - other.c),
            )
        }

        fun getAntinodesWith(other: Antenna, maxH: Int, maxW: Int): List<Coordinate> {
            val rDiff = other.r - r
            val cDiff = other.c - c

            val result = mutableListOf<Coordinate>()

            var tempR = r
            var tempC = c

            result.add(location)
            while (tempR in (0..<maxH) && tempC in (0..<maxW)) {
                result.add(Coordinate(row = tempR - rDiff, col = tempC - cDiff))
                tempR -= rDiff
                tempC -= cDiff
            }

            tempR = other.r
            tempC = other.c
            result.add(other.location)
            while (tempR in (0..<maxH) && tempC in (0..<maxW)) {
                result.add(Coordinate(row = tempR + rDiff, col = tempC + cDiff))
                tempR += rDiff
                tempC += cDiff
            }

            return result
        }
    }

    data class Coordinate(
        val row: Int,
        val col: Int,
    ) {
        fun isInside(maxH: Int, maxW: Int): Boolean = row in (0..<maxH) && col in (0..<maxW)
    }
}

fun main() {
    Day08().apply {
        part1()
        part2()
    }
}
