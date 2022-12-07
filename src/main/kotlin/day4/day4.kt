@file:Suppress("unused")
package day4

fun IntRange.contains(other: IntRange): Boolean = start <= other.first && last >= other.last

fun IntRange.containsAny(other: IntRange): Boolean = start <= other.last && last >= other.first

fun createRange(input: String): IntRange {
    val values = input.split("-")
    return IntRange(values[0].toInt(), values[1].toInt())
}

fun part1(lines: List<String>): Any {
    return lines.filter { it.isNotBlank() }.count {
        val parts = it.split(",")
        val range1 = createRange(parts[0])
        val range2 = createRange(parts[1])
        range1.contains(range2) || range2.contains(range1)
    }
}

fun part2(lines: List<String>): Any {
    return lines.filter { it.isNotBlank() }.count {
        val parts = it.split(",")
        val range1 = createRange(parts[0])
        val range2 = createRange(parts[1])
        range1.containsAny(range2)
    }
}