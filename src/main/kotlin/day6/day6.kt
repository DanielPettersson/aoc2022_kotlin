@file:Suppress("unused")
package day6

fun findMarker(line: String, sequenceLength: Int): Int {
    for (i in sequenceLength..line.lastIndex) {
        val fourChars = line.subSequence(i - sequenceLength, i)
        if (fourChars.toSet().size == sequenceLength) {
            return i
        }
    }
    throw IllegalStateException("Found no marker")
}

fun part1(lines: List<String>): Any {
    return findMarker(lines[0], 4)
}

fun part2(lines: List<String>): Any {
    return findMarker(lines[0], 14)
}