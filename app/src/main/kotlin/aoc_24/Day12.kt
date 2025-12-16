package aoc_24

import java.util.concurrent.atomic.AtomicInteger

class Day12 {

    companion object {
        private const val RESOURCE_BASENAME = "day12"
        private const val RESOURCE_INPUT = "${RESOURCE_BASENAME}_input.txt"
    }

    fun part1() {
        println("=== Part 1 ===")
        val input1 = getResourceAsStringCollection("${RESOURCE_BASENAME}_test.txt")
        val result1 = input1.calculatePrice()
        println("test result 1 - $result1")
        if (result1 != 140) throw Exception("example 1 not passing")

        val input2 = getResourceAsStringCollection("${RESOURCE_BASENAME}_test2.txt")
        val result2 = input2.calculatePrice()
        println("test result 2 - $result2")
        if (result2 != 772) throw Exception("example 2 not passing")

        val input3 = getResourceAsStringCollection("${RESOURCE_BASENAME}_test3.txt")
        val result3 = input3.calculatePrice()
        println("test result 3 - $result3")
        if (result3 != 1930) throw Exception("example 3 not passing")

        val input = getResourceAsStringCollection(RESOURCE_INPUT)
        val result = input.calculatePrice()
        println("result - $result")
    }

    fun part2() {
        println("=== Part 2 ===")
        val input1 = getResourceAsStringCollection("${RESOURCE_BASENAME}_test.txt")
        val result1 = input1.calculateDiscountPrice()
        println("test result 1 - $result1")
        if (result1 != 80) throw Exception("example 1 not passing")

        val input2 = getResourceAsStringCollection("${RESOURCE_BASENAME}_test2.txt")
        val result2 = input2.calculateDiscountPrice()
        println("test result 2 - $result2")
        if (result2 != 436) throw Exception("example 2 not passing")

        val input3 = getResourceAsStringCollection("${RESOURCE_BASENAME}_test3.txt")
        val result3 = input3.calculateDiscountPrice()
        println("test result 3 - $result3")
        if (result3 != 1206) throw Exception("example 3 not passing")

        val input = getResourceAsStringCollection(RESOURCE_INPUT)
        val result = input.calculateDiscountPrice()
        println("result - $result")
    }

    private fun List<String>.calculatePrice(): Int = toRegions().sumOf { it.price() }

    private fun List<String>.calculateDiscountPrice(): Int = toRegions().sumOf { it.discountPrice() }

    private fun List<String>.toRegions(): List<Region> {
        val result = mutableListOf<Region>()

        this.forEachIndexed { rowIndex, line ->
            line.forEachIndexed { colIndex, c ->
                val plot = Plot(rowIndex, colIndex, c)

                var matchingRegion = result
                    .filter { it.plant == c }
                    .findLast { r -> r.plant == c && r.hasPlotAt(rowIndex, colIndex - 1) }

                if (matchingRegion == null && rowIndex > 0) {
                    // check previous line at the same index
                    matchingRegion = result
                        .filter { it.plant == c }
                        .findLast { r -> r.hasPlotAt(rowIndex - 1, colIndex) }
                }

                if (matchingRegion != null) {
                    matchingRegion.add(plot)
                } else {
                    val region = Region()
                    region.add(plot)
                    result.add(region)
                }
            }
        }

        this.indices.reversed().forEach { rowIndex ->
            this[rowIndex].indices.reversed().forEach { colIndex ->
                val c = this[rowIndex][colIndex]
                val region = result.find { it.plant == c && it.hasPlotAt(rowIndex, colIndex) }
                requireNotNull(region)

                val nearbyRegion = result.find { it.plant == c && it.hasPlotAt(rowIndex - 1, colIndex) }
                if (nearbyRegion != null && nearbyRegion.id != region.id) {
                    //merge region plots and remove region
                    region.plots.forEach { nearbyRegion.add(it) }
                    result.remove(region)
                }
            }
        }

        return result
    }

    private data class Region(
        val id: Int = ID_BASE.incrementAndGet(),
    ) {
        companion object {
            private val ID_BASE = AtomicInteger(0)
        }

        val plotHashMap = hashMapOf<Pair<Int, Int>, Plot>()
        val plots: MutableList<Plot> = mutableListOf()
        val plant: Char get() = plots.first().plant

        fun add(plot: Plot) {
            plots.add(plot)
            plotHashMap[plot.row to plot.col] = plot
        }

        fun hasPlotAt(row: Int, col: Int): Boolean =
            plotHashMap.containsKey(row to col)

        private fun perimeter(): Int {
            var total = 0
            plots.forEach { p ->
                if (plotHashMap[p.row - 1 to p.col] == null) total++
                if (plotHashMap[p.row + 1 to p.col] == null) total++
                if (plotHashMap[p.row to p.col - 1] == null) total++
                if (plotHashMap[p.row to p.col + 1] == null) total++
            }
            return total
        }

        private fun edges(): Int {
            var total = 0
            plots.forEach { p ->
                // top-left
                if (!hasPlotAt(p.row - 1, p.col) && !hasPlotAt(p.row, p.col - 1)) total++
                // bottom-left
                if (!hasPlotAt(p.row + 1, p.col) && !hasPlotAt(p.row, p.col - 1)) total++
                // top-right
                if (!hasPlotAt(p.row - 1, p.col) && !hasPlotAt(p.row, p.col + 1)) total++
                // bottom-right
                if (!hasPlotAt(p.row + 1, p.col) && !hasPlotAt(p.row, p.col + 1)) total++

                // inner |_
                //
                if (hasPlotAt(p.row - 1, p.col) && hasPlotAt(p.row, p.col + 1) && !hasPlotAt(p.row - 1, p.col + 1)) total++
                // inner _|
                //
                if (hasPlotAt(p.row - 1, p.col) && hasPlotAt(p.row, p.col - 1) && !hasPlotAt(p.row - 1, p.col - 1)) total++
                // inner _
                //        |
                if (hasPlotAt(p.row + 1, p.col) && hasPlotAt(p.row, p.col - 1) && !hasPlotAt(p.row + 1, p.col - 1)) total++
                // inner   _
                //        |
                if (hasPlotAt(p.row + 1, p.col) && hasPlotAt(p.row, p.col + 1) && !hasPlotAt(p.row + 1, p.col + 1)) total++
            }
            return total
        }

        fun price(): Int {
            val peri = perimeter()
            val count = plots.size
            val price = peri * count
//            println("$id/$plant:perimeter=$peri, plots=${count}, price=$price")
            return price
        }

        fun discountPrice(): Int {
            val edges = edges()
            val count = plots.size
            val price = edges * count
//            println("$id/$plant:edges=$edges, plots=${count}, price=$price")
            return price
        }

        override fun toString(): String = "Region(id=$id,plant=$plant,$plots)"
    }

    private data class Plot(
        val row: Int,
        val col: Int,
        val plant: Char,
    ) {
        override fun toString(): String = "[$row,$col]"
    }
}

fun main() {
    Day12().apply {
        part1()
        part2()
    }
}
