package aoc_24

class Day06 {

    companion object {
        private const val RESOURCE_BASENAME = "day06"
        private const val RESOURCE_TEST = "${RESOURCE_BASENAME}_test.txt"
        private const val RESOURCE_INPUT = "${RESOURCE_BASENAME}_input.txt"
    }

    fun part1() {
        println("=== Part 1 ===")
        val testInput = getResourceAsStringCollection(RESOURCE_TEST)
        val testResult = getPart1(testInput)
        println("test result - $testResult")
        if (testResult != 41) throw Exception("example 1 not passing")

        val input = getResourceAsStringCollection(RESOURCE_INPUT)
        val result = getPart1(input)
        println("result - $result")
    }

    fun part2() {
        println("=== Part 2 ===")
        val testInput = getResourceAsStringCollection(RESOURCE_TEST)
        val testResult = getPart2(testInput)
        println("test result - $testResult")
        if (testResult != 6) throw Exception("example 2 not passing")

        val input = getResourceAsStringCollection(RESOURCE_INPUT)
        val result = getPart2(input)
        println("result - $result")
    }

    private fun getPart1(input: List<String>): Int {
        val visitedPlaces = mutableSetOf<Pair<Int, Int>>()
        val obstructions = mutableSetOf<Pair<Int, Int>>()
        var currentPos: Pair<Int, Int> = -1 to -1
        var direction = "U"

        val width = input.first().length
        val height = input.size

        input.forEachIndexed { rowIndex, line ->
            line.forEachIndexed { colIndex, c ->
                if (c == '#') obstructions.add(rowIndex to colIndex)
                if (c == '^') currentPos = rowIndex to colIndex
            }
        }

        if (currentPos.first == -1 || currentPos.second == -1) return 0

        visitedPlaces.add(currentPos)

        while (direction != "") {
            // move up
            if (direction == "U") {
                val here = currentPos
                val obstr = obstructions.filter { it.second == here.second && it.first < here.first }
                    .sortedBy { it.first }
                    .lastOrNull()

                // exited up
                if (obstr == null) {
                    (0..here.first).forEach { visitedPlaces.add(it to here.second) }
                    direction = ""
                } else {
                    (obstr.first+1..here.first).forEach { visitedPlaces.add(it to here.second) }
                    currentPos = obstr.first + 1 to obstr.second
                    direction = "R"
                }
            }

            // move right
            if (direction == "R") {
                val here = currentPos
                val obstr = obstructions.filter { it.first == here.first && it.second > here.second }
                    .sortedBy { it.second }
                    .firstOrNull()

                // exited right
                if (obstr == null) {
                    (here.second..<width).forEach { visitedPlaces.add(here.first to it) }
                    direction = ""
                } else {
                    (here.second..<obstr.second).forEach { visitedPlaces.add(here.first to it) }
                    currentPos = obstr.first to obstr.second - 1
                    direction = "D"
                }
            }

            // move down
            if (direction == "D") {
                val here = currentPos
                val obstr = obstructions.filter { it.second == here.second && it.first > here.first }
                    .sortedBy { it.first }
                    .firstOrNull()

                // exited down
                if (obstr == null) {
                    (here.first..<height).forEach { visitedPlaces.add(it to here.second) }
                    direction = ""
                } else {
                    (here.first..<obstr.first).forEach { visitedPlaces.add(it to here.second) }
                    currentPos = obstr.first - 1 to obstr.second
                    direction = "L"
                }
            }

            // move left
            if (direction == "L") {
                val here = currentPos
                val obstr = obstructions.filter { it.first == here.first && it.second < here.second }
                    .sortedBy { it.second }
                    .lastOrNull()

                // exited left
                if (obstr == null) {
                    (0..here.second).forEach { visitedPlaces.add(here.first to it) }
                    direction = ""
                } else {
                    (obstr.second+1..here.second).forEach { visitedPlaces.add(here.first to it) }
                    currentPos = obstr.first to obstr.second + 1
                    direction = "U"
                }
            }
        }

        return visitedPlaces.size
    }

    private fun getPart2(input: List<String>): Int {
        val visitedPlaces = mutableSetOf<Pair<Int, Int>>()
        val obstructions = mutableSetOf<Pair<Int, Int>>()
        var currentPos: Pair<Int, Int> = -1 to -1
        var direction = "U"
        var hasTurned = false

        val width = input.first().length
        val height = input.size

        input.forEachIndexed { rowIndex, line ->
            line.forEachIndexed { colIndex, c ->
                if (c == '#') obstructions.add(rowIndex to colIndex)
                if (c == '^') currentPos = rowIndex to colIndex
            }
        }

        if (currentPos.first == -1 || currentPos.second == -1) return 0

        val start = currentPos

        while (direction != "") {
            // move up
            if (direction == "U") {
                val here = currentPos
                val obstr = obstructions.filter { it.second == here.second && it.first < here.first }
                    .sortedBy { it.first }
                    .lastOrNull()

                // exited up
                if (obstr == null) {
                    (0..here.first).forEach { visitedPlaces.add(it to here.second) }
                    direction = ""
                } else {
                    if (hasTurned) {
                        (obstr.first + 1..here.first).forEach { visitedPlaces.add(it to here.second) }
                    }
                    currentPos = obstr.first + 1 to obstr.second
                    direction = "R"
                    hasTurned = true
                }
            }

            // move right
            if (direction == "R") {
                val here = currentPos
                val obstr = obstructions.filter { it.first == here.first && it.second > here.second }
                    .sortedBy { it.second }
                    .firstOrNull()

                // exited right
                if (obstr == null) {
                    (here.second..<width).forEach { visitedPlaces.add(here.first to it) }
                    direction = ""
                } else {
                    (here.second..<obstr.second).forEach { visitedPlaces.add(here.first to it) }
                    currentPos = obstr.first to obstr.second - 1
                    direction = "D"
                }
            }

            // move down
            if (direction == "D") {
                val here = currentPos
                val obstr = obstructions.filter { it.second == here.second && it.first > here.first }
                    .sortedBy { it.first }
                    .firstOrNull()

                // exited down
                if (obstr == null) {
                    (here.first..<height).forEach { visitedPlaces.add(it to here.second) }
                    direction = ""
                } else {
                    (here.first..<obstr.first).forEach { visitedPlaces.add(it to here.second) }
                    currentPos = obstr.first - 1 to obstr.second
                    direction = "L"
                }
            }

            // move left
            if (direction == "L") {
                val here = currentPos
                val obstr = obstructions.filter { it.first == here.first && it.second < here.second }
                    .sortedBy { it.second }
                    .lastOrNull()

                // exited left
                if (obstr == null) {
                    (0..here.second).forEach { visitedPlaces.add(here.first to it) }
                    direction = ""
                } else {
                    (obstr.second+1..here.second).forEach { visitedPlaces.add(here.first to it) }
                    currentPos = obstr.first to obstr.second + 1
                    direction = "U"
                }
            }
        }

        println("visited ${visitedPlaces.size}")
        val newObstructionPlaces = mutableSetOf<Pair<Int, Int>>()
        visitedPlaces.forEach { (row, col) ->
            val testSet = obstructions + (row to col)
            if (isLooped(testSet, start)) {
                newObstructionPlaces.add(row to col)
            }
        }

        return newObstructionPlaces.size
    }

    private fun isLooped(
        obstructions: Set<Pair<Int, Int>>,
        start: Pair<Int, Int>,
    ): Boolean {
        val turnsUp = mutableSetOf<Pair<Int, Int>>()
        val turnsRight = mutableSetOf<Pair<Int, Int>>()
        val turnsDown = mutableSetOf<Pair<Int, Int>>()
        val turnsLeft = mutableSetOf<Pair<Int, Int>>()

        var direction = "U"
        var currentPos = start

        while (direction != "") {
            val here = currentPos
            val obstr = obstructions.findNext(direction, here)

            if (direction == "U") {
                // exited up
                if (obstr == null) {
                    direction = ""
                } else {
                    if (turnsRight.contains(obstr)) return true
                    turnsRight.add(obstr)
                    currentPos = obstr.first + 1 to obstr.second
                    direction = "R"
                }
            } else if (direction == "R") {
                // exited right
                if (obstr == null) {
                    direction = ""
                } else {
                    if (turnsDown.contains(obstr)) return true
                    turnsDown.add(obstr)
                    currentPos = obstr.first to obstr.second - 1
                    direction = "D"
                }
            } else if (direction == "D") {
                // exited down
                if (obstr == null) {
                    direction = ""
                } else {
                    if (turnsLeft.contains(obstr)) return true
                    turnsLeft.add(obstr)
                    currentPos = obstr.first - 1 to obstr.second
                    direction = "L"
                }
            } else if (direction == "L") {
                // exited left
                if (obstr == null) {
                    direction = ""
                } else {
                    if (turnsUp.contains(obstr)) return true
                    turnsUp.add(obstr)
                    currentPos = obstr.first to obstr.second + 1
                    direction = "U"
                }
            }
        }

        return false
    }

    private fun Set<Pair<Int, Int>>.findNext(direction: String, from: Pair<Int, Int>): Pair<Int, Int>? {
        return when (direction) {
            "U" -> {
                this.filter { it.second == from.second && it.first < from.first }
                    .sortedBy { it.first }
                    .lastOrNull()
            }
            "R" -> {
                this.filter { it.first == from.first && it.second > from.second }
                    .sortedBy { it.second }
                    .firstOrNull()
            }
            "D" -> {
                this.filter { it.second == from.second && it.first > from.first }
                    .sortedBy { it.first }
                    .firstOrNull()
            }
            "L" -> {
                this.filter { it.first == from.first && it.second < from.second }
                    .sortedBy { it.second }
                    .lastOrNull()
            }
            else -> throw IllegalArgumentException()
        }
    }
}

fun main() {
    Day06().apply {
        part1()
        part2()
    }
}
