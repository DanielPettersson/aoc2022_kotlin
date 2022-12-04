@file:Suppress("unused")

import java.lang.Integer.parseInt

fun day1Part2(lines: List<String>): Any {
    return lines.fold(mutableListOf(1)) { acc, line ->
        if (line == "") {
            acc.add(0)
        } else {
            acc[acc.lastIndex] = acc.last() + parseInt(line)
        }
        acc
    }.sortedDescending().take(3).sum()
}