@file:Suppress("unused")

package day9

import java.lang.IllegalArgumentException
import kotlin.math.pow
import kotlin.math.sqrt

data class Pos(val x: Int, val y: Int) {
    fun distanceFrom(p: Pos): Double {
        val xd =  (x - p.x).toDouble()
        val yd =  (y - p.y).toDouble()
        return sqrt(xd.pow(2) + yd.pow(2))
    }

    fun add(p: Pos): Pos = Pos(x + p.x, y + p.y)
}

fun parseMoves(line: String): List<Pos> {
    val (dir, num) = line.split(" ")
    return when (dir) {
        "U" -> List(num.toInt()) { Pos(0, -1) }
        "D" -> List(num.toInt()) { Pos(0, 1) }
        "L" -> List(num.toInt()) { Pos(-1, 0) }
        "R" -> List(num.toInt()) { Pos(1, 0) }
        else -> throw IllegalArgumentException("Illegal dir: '$dir'")
    }
}

val possibleTailMoves = listOf(
    Pos(-1, -1), Pos(0, -1), Pos(1, -1),
    Pos(-1, 0),                    Pos(1, 0),
    Pos(-1, 1), Pos(0, 1), Pos(1, 1)
)

fun tailMove(h: Pos, t: Pos): Pos {
    val d = h.distanceFrom(t)
    if (d < 2) return Pos(0, 0)
    return possibleTailMoves.minBy { h.distanceFrom(t.add(it)) }
}

fun part1(lines: List<String>): Any {
    val moves = lines.filter { it.isNotBlank() }.flatMap(::parseMoves)
    var h = Pos(0, 0)
    var t = Pos(0, 0)
    val tVisited = mutableSetOf(t)

    moves.forEach {
        h = h.add(it)
        t = t.add(tailMove(h, t))
        tVisited.add(t)
    }
    return tVisited.size
}

fun part2(lines: List<String>): Any {
    val moves = lines.filter { it.isNotBlank() }.flatMap(::parseMoves)
    val knots = MutableList(10) { Pos(0, 0) }
    val tVisited = mutableSetOf(knots[knots.lastIndex])

    moves.forEach {
        knots[0] = knots[0].add(it)
        for (i in 1..knots.lastIndex) {
            knots[i] = knots[i].add(tailMove(knots[i-1], knots[i]))
        }
        tVisited.add(knots[knots.lastIndex])
    }
    return tVisited.size
}

