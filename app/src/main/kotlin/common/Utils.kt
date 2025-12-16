package aoc_24

fun getResourceAsStringCollection(resourceName: String) =
    object {}::class.java.classLoader.getResourceAsStream(resourceName)
            ?.bufferedReader()
            ?.readLines()
            ?: emptyList()

fun String.splitToWords(): List<String> =
    this.split(" ")

fun String.splitToDigits(): List<Int> =
    this.split(" ").map { c -> c.toInt() }