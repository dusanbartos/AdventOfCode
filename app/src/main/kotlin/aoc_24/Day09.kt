package aoc_24

class Day09 {

    companion object {
        private const val RESOURCE_BASENAME = "day09"
        private const val RESOURCE_TEST = "${RESOURCE_BASENAME}_test.txt"
        private const val RESOURCE_INPUT = "${RESOURCE_BASENAME}_input.txt"
    }

    fun part1() {
        println("=== Part 1 ===")
        val testInput = getResourceAsStringCollection(RESOURCE_TEST)
        val testResult = getPart1(testInput)
        println("test result - $testResult")
        if (testResult != 1928L) throw Exception("example 1 not passing")

        val input = getResourceAsStringCollection(RESOURCE_INPUT)
        val result = getPart1(input)
        println("result - $result")
    }

    fun part2() {
        println("=== Part 2 ===")
        val testInput = getResourceAsStringCollection(RESOURCE_TEST)
        val testResult = getPart2(testInput)
        println("test result - $testResult")
        if (testResult != 2858L) throw Exception("example 2 not passing")

        val input = getResourceAsStringCollection(RESOURCE_INPUT)
        val result = getPart2(input)
        println("result - $result")
    }

    private fun getPart1(input: List<String>): Long {
        val diskMap = input.first().toDiskMap()
        val compressed = diskMap.compress()
        return compressed.toCheckSum()
    }

    private fun getPart2(input: List<String>): Long {
        val diskMap = input.first().toDiskMap()
        val compressed = diskMap.fileCompress()
        return compressed.toCheckSum()
    }

    private fun String.toDiskMap(): List<Int> {
        val data = mutableListOf<Int>()

        this.forEachIndexed { index, c ->
            val x = c.digitToInt()
            if (index % 2 == 0) {
                repeat(x) { data.add(index / 2) }
            } else {
                repeat(x) { data.add(-1) }
            }
        }

        return data
    }

    private fun List<Int>.compress(): List<Int> {
        val result = mutableListOf<Int>()

        var frontIndex = 0
        var backIndex = this.indexOfLast { it != -1 }

        while (frontIndex <= backIndex) {
            if (this[frontIndex] == -1) {
                result.add(this[backIndex])
                backIndex--
                while (this[backIndex] == -1) {
                    backIndex--
                }
            } else {
                result.add(this[frontIndex])
            }
            frontIndex++
        }
        return result
    }

    private fun List<Int>.fileCompress(): List<Int> {
        val result = this.toMutableList()

        var fileId = this.last { it != -1 }
        var fileLength = this.count { it == fileId }
        var fileIndex = this.indexOfFirst { it == fileId }

        while (fileId >= 0) {
            val freeSpaceStart = result.indices.firstOrNull { index ->
                index + fileLength < result.size &&
                        index < fileIndex &&
                        result.subList(index, index + fileLength).all { it == -1 }
            }

            if (freeSpaceStart != null) {
                repeat(fileLength) {
                    result[freeSpaceStart + it] = fileId
                    result[fileIndex + it] = -1
                }
            }

            fileId--
            fileLength = this.count { it == fileId }
            fileIndex = this.indexOfFirst { it == fileId }
        }
        return result
    }

    private fun List<Int>.toCheckSum(): Long {
        return this.indices.sumOf { index -> index * (this[index].takeIf { it != -1 } ?: 0).toLong() }
    }
}

fun main() {
    Day09().apply {
        part1()
        part2()
    }
}
