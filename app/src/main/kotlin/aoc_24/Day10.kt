package aoc_24

class Day10 {

    companion object {
        private const val RESOURCE_BASENAME = "day10"
        private const val RESOURCE_TEST = "${RESOURCE_BASENAME}_test.txt"
        private const val RESOURCE_INPUT = "${RESOURCE_BASENAME}_input.txt"
    }

    fun part1() {
        println("=== Part 1 ===")
        val testInput = getResourceAsStringCollection(RESOURCE_TEST)
        val testResult = getPart1(testInput)
        println("test result - $testResult")
        if (testResult != 36) throw Exception("example 1 not passing")

        val input = getResourceAsStringCollection(RESOURCE_INPUT)
        val result = getPart1(input)
        println("result - $result")
    }

    fun part2() {
        println("=== Part 2 ===")
        val testInput = getResourceAsStringCollection(RESOURCE_TEST)
        val testResult = getPart2(testInput)
        println("test result - $testResult")
        if (testResult != 81) throw Exception("example 2 not passing")

        val input = getResourceAsStringCollection(RESOURCE_INPUT)
        val result = getPart2(input)
        println("result - $result")
    }

    private fun getPart1(input: List<String>): Int {
        val topography = input.map { line -> line.map { c -> c.digitToInt() } }
        val trailheads = input.getTrailheads()
        return trailheads.sumOf { it.getScore(topography) }
    }

    private fun getPart2(input: List<String>): Int {
//        val topography = input.map { line -> line.map { c -> c.digitToInt() } }
        val topography = input.map { line -> line.map { c -> c.digitToIntOrNull() ?: -1 } }
        val trailheads = input.getTrailheads()
        return trailheads.sumOf { it.getRating(topography) }
    }

    private fun List<String>.getTrailheads(): List<Coordinate> {
        return this.flatMapIndexed { rowIndex, line ->
            line.mapIndexedNotNull { colIndex, c ->
                if (c == '0') Coordinate(row = rowIndex, col = colIndex, level = c.digitToInt()) else null
            }
        }
    }

    data class Coordinate(
        val row: Int,
        val col: Int,
        val level: Int,
    ) {
        fun getScore(topography: List<List<Int>>): Int {
            val hits = mutableSetOf<Coordinate>()

            var i = 0
            var vertices = setOf(this)
            while(i < 9) {
                vertices = vertices.flatMap { it.getNearbyVertices(i, topography) }.toSet()
                i++
            }

            hits.addAll(vertices)
            return hits.size
        }

        fun getRating(topography: List<List<Int>>): Int {
            val trails = mutableListOf<List<Coordinate>>()
            trails.add(mutableListOf(this))

            var i = 0
            var vertices = setOf(this)
            while(i < 9) {
                vertices = vertices.flatMap { v ->
                    val origins = trails.filter { it.last() == v }
                    val nb = v.getNearbyVertices(i, topography)
                    nb.forEach { n -> trails.addAll(origins.map { o -> o + n }) }
                    nb
                }.toSet()

                trails.removeAll { it.size <= i }
                i++
            }

            val validTrails = trails.filter { it.size == 10 }
            return validTrails.size
        }

        private fun getNearbyVertices(i: Int, topography: List<List<Int>>): List<Coordinate> {
            val vertices = mutableListOf<Coordinate>()
            if (row + 1 < topography.size && topography[row + 1][col] == i + 1) vertices.add(Coordinate(row + 1, col, topography[row+1][col]))
            if (row > 0 && topography[row - 1][col] == i + 1) vertices.add(Coordinate(row - 1, col, topography[row-1][col]))
            if (col + 1 < topography[row].size && topography[row][col + 1] == i + 1) vertices.add(Coordinate(row, col + 1, topography[row][col+1]))
            if (col > 0 && topography[row][col - 1] == i + 1) vertices.add(Coordinate(row, col - 1, topography[row][col-1]))
//            println("nearby i=$i vertices=$vertices $this")
            return vertices
        }
    }
}

fun main() {
    Day10().apply {
        part1()
        part2()
    }
}
