@file:Suppress("unused")
package day1

import java.lang.Integer.parseInt

fun part2(lines: List<String>): Any {
    return lines.fold(mutableListOf(1)) { acc, line ->
        if (line == "") {
            acc.add(0)
        } else {
            acc[acc.lastIndex] = acc.last() + parseInt(line)
        }
        acc
    }.sortedDescending().take(3).sum()
}