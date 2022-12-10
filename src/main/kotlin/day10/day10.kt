@file:Suppress("unused")

package day10

import kotlin.math.abs

fun parseOp(line: String): List<Int> {
    val parts = line.split(" ")
    return when (parts[0]) {
        "noop" -> listOf(0)
        "addx" -> listOf(0, parts[1].toInt())
        else -> throw IllegalArgumentException("Illegal op line: $line")
    }
}

fun part1(lines: List<String>): Any {
    val ops = lines.filter { it.isNotBlank() }.flatMap { parseOp(it) }
    var x = 1

    ops.forEachIndexed { i, op ->
        if (i % 40 == 0) print("\n")
        if (abs(x - i % 40) <= 1) print("#") else print(".")
        x += op
    }

    return 0
}
