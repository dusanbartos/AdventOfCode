package aoc_24

class Day15 {

    companion object {
        private const val RESOURCE_BASENAME = "day15"
        private const val RESOURCE_TEST_1 = "${RESOURCE_BASENAME}_test.txt"
        private const val RESOURCE_TEST_2 = "${RESOURCE_BASENAME}_test2.txt"
        private const val RESOURCE_INPUT = "${RESOURCE_BASENAME}_input.txt"
    }

    fun part1() {
        println("=== Part 1 ===")
        val testInput1 = getResourceAsStringCollection(RESOURCE_TEST_1)
        val testResult1 = testInput1.getCoordinateSum()
        println("test result - $testResult1")
        if (testResult1 != 2028) throw Exception("example 1.1 not passing")

        val testInput2 = getResourceAsStringCollection(RESOURCE_TEST_2)
        val testResult2 = testInput2.getCoordinateSum()
        println("test result - $testResult2")
        if (testResult2 != 10092) throw Exception("example 1.2 not passing")

        val input = getResourceAsStringCollection(RESOURCE_INPUT)
        val result = input.getCoordinateSum()
        println("result - $result")
    }

    fun part2() {
        println("=== Part 2 ===")
    }

    private fun List<String>.getCoordinateSum(): Int {
        val input = Input.from(this)

        // move robot according instructions
        val map = input.map.toMutableMap()
        var robot = input.robot
        input.instructions.forEach { dir ->
            val nextCoordinate = when(dir) {
               '<' -> Coordinate(robot.row, robot.col - 1)
               '>' -> Coordinate(robot.row, robot.col + 1)
               '^' -> Coordinate(robot.row - 1, robot.col)
               'v' -> Coordinate(robot.row + 1, robot.col)
                else -> null
            }
            requireNotNull(nextCoordinate)

            val next = map[nextCoordinate]
            when (next) {
                // move robot
                null -> robot = nextCoordinate
                '#' -> { /* do nothing, robot hit the wall */ }
                'O' -> {
                    // find where we can move the box
                    // if there are multiple boxes, it doesn't matter if we shift all,
                    // or just move the first one and place it to the nearest empty space
                    val closestSpace = map.findClosestSpaceOrNull(robot, dir)
                    if (closestSpace != null) {
                        map.remove(nextCoordinate)
                        map[closestSpace] = 'O'
                        robot = nextCoordinate
                    }
                }
            }
        }

        // calculate GPS
        return map.filter { it.value == 'O' }.entries.sumOf { it.key.sum() }
    }

    private fun Map<Coordinate, Char>.findClosestSpaceOrNull(
        from: Coordinate,
        dir: Char,
    ): Coordinate? {
        return when(dir) {
            '<' -> {
                ((from.col - 1).downTo(0)).forEach { i ->
                    val where = Coordinate(from.row, i)
                    val candidate = this[where] ?: return where
                    if (candidate == '#') return null
                }
                throw IllegalStateException("Are there no walls?")
            }
            '>' -> {
                val max = this.filter { it.key.row == from.row }.map { it.key.col }.max()
                ((from.col + 1)..max).forEach { i ->
                    val where = Coordinate(from.row, i)
                    val candidate = this[where] ?: return where
                    if (candidate == '#') return null
                }
                throw IllegalStateException("Are there no walls?")
            }
            '^' -> {
                ((from.row - 1).downTo(0)).forEach { i ->
                    val where = Coordinate(i, from.col)
                    val candidate = this[where] ?: return where
                    if (candidate == '#') return null
                }
                throw IllegalStateException("Are there no walls?")
            }
            'v' -> {
                val max = this.filter { it.key.col == from.col }.map { it.key.row }.max()
                ((from.row + 1)..max).forEach { i ->
                    val where = Coordinate(i, from.col)
                    val candidate = this[where] ?: return where
                    if (candidate == '#') return null
                }
                throw IllegalStateException("Are there no walls?")
            }
            else -> null
        }
    }

    data class Coordinate(val row: Int, val col: Int) {
        fun sum(): Int = 100 * row + col
    }

    data class Input(
        val robot: Coordinate,
        val instructions: String,
        val map: Map<Coordinate, Char>,
    ) {
        companion object {
            fun from(file: List<String>): Input {
                val area = file.takeWhile { it.isNotBlank() }
                val instructions = file.drop(area.size + 1).joinToString("")

                var robot: Coordinate? = null
                val map = mutableMapOf<Coordinate, Char>()

                area.forEachIndexed { row, line ->
                    line.forEachIndexed { col, c ->
                        when (c) {
                            '#' -> map[Coordinate(row, col)] = '#'
                            'O' -> map[Coordinate(row, col)] = 'O'
                            '@' -> robot = Coordinate(row, col)
                        }
                    }
                }

                return Input(
                    robot = requireNotNull(robot) { "Robot not found on the map" },
                    instructions = instructions,
                    map = map,
                )
            }
        }
    }
}

fun main() {
    Day15().apply {
        part1()
        part2()
    }
}
