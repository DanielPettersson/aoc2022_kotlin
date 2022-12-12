@file:Suppress("unused")

package day12

import path.Graph
import path.GraphImpl
import path.shortestPath
import java.lang.Double.min
import java.util.*

data class Coord(
    val x: Int,
    val y: Int
)

fun charToHeight(c: Char): Int = when (c) {
    'S' -> 0
    'E' -> 25
    else -> c.code - 97
}

data class ParseResult(val start: List<Coord>, val end: Coord, val graph: Graph<Coord, Int>)

fun parseGrid(lines: List<String>, isStartFnc: (Char) -> Boolean): ParseResult {

    val heights = lines.map { line ->
        line.map { charToHeight(it) }
    }

    val getRoute: (Coord, Coord) -> Optional<Pair<Coord, Coord>> = { from, to ->
        if (heights[to.y][to.x] - heights[from.y][from.x] <= 1) {
            Optional.of(Pair(from, to))
        } else {
            Optional.empty()
        }
    }

    val starts = mutableListOf<Coord>()
    var end = Coord(0, 0)

    val graph = GraphImpl<Coord, Int>(directed = true, defaultCost = 1)

    lines.forEachIndexed { y, line ->
        line.forEachIndexed { x, c ->
            val coord = Coord(x, y)

            if (isStartFnc(c)) starts.add(coord)
            if (c == 'E') end = coord

            // UP
            if (y > 0) {
                getRoute(coord, Coord(x, y - 1)).ifPresent { graph.addArc(it) }
            }
            // DOWN
            if (y < heights.lastIndex) {
                getRoute(coord, Coord(x, y + 1)).ifPresent { graph.addArc(it) }
            }
            // LEFT
            if (x > 0) {
                getRoute(coord, Coord(x - 1, y)).ifPresent { graph.addArc(it) }
            }
            // RIGHT
            if (x < heights[0].lastIndex) {
                getRoute(coord, Coord(x + 1, y)).ifPresent { graph.addArc(it) }
            }

        }
    }

    return ParseResult(starts, end, graph)
}

fun part1(lines: List<String>): Any {
    val (starts, end, graph) = parseGrid(lines.filter { it.isNotBlank() }) { it == 'S' }
    val (_, length) = shortestPath(graph, starts[0], end)
    return length
}

fun part2(lines: List<String>): Any {
    val (starts, end, graph) = parseGrid(lines.filter { it.isNotBlank() }) { it == 'S' || it == 'a' }

    var shortestPath = Double.MAX_VALUE

    println("numStarts: ${starts.size}")
    starts.forEachIndexed { i, start ->
        val (_, length) = shortestPath(graph, start, end)
        println("num: $i, start: $start, length: $length, shortest: $shortestPath")

        shortestPath = min(shortestPath, length)
    }


    return shortestPath
}