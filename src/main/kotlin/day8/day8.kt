@file:Suppress("unused")

package day8

import kotlin.math.max


fun parseLines(lines: List<String>): List<List<Int>> =
    lines.filter { it.isNotBlank() }.map { it.map { c -> c.toString().toInt() } }

data class Tree(val x: Int, val y: Int)

fun lookForTrees(trees: List<Int>, progression: IntProgression): List<Int> {
    var max = -1
    return progression.filter {
        val visible = trees[it] > max
        if (visible) max = trees[it]
        visible
    }
}

fun part1(lines: List<String>): Any {
    val visibleTrees = HashSet<Tree>()

    val trees = parseLines(lines)
    val height = trees.size
    val width = trees[0].size

    for (y in 0 until height) {
        val progression = 0 until width
        lookForTrees(trees[y], progression).forEach { visibleTrees.add(Tree(it, y))}
        lookForTrees(trees[y], progression.reversed()).forEach { visibleTrees.add(Tree(it, y))}
    }
    for (x in 0 until width) {
        val progression = 0 until height
        val col = progression.map { trees[it][x] }
        lookForTrees(col, progression).forEach { visibleTrees.add(Tree(x, it))}
        lookForTrees(col, progression.reversed()).forEach { visibleTrees.add(Tree(x, it))}
    }

    return visibleTrees.size
}

fun viewingDistance(height: Int, trees: List<Int>, progression: IntProgression): Int {
    if (progression.isEmpty()) return 0
    var steps = 0
    for (i in progression) {
        steps++
        if (trees[i] >= height) break
    }
    return steps
}

fun part2(lines: List<String>): Any {
    val trees = parseLines(lines)
    val height = trees.size
    val width = trees[0].size

    var maxScenicScore = 0

    for (y in 0 until height) {
        for (x in 0 until width) {
            val row = trees[y]
            val treeHeight = row[x]
            val col = (0 until height).map { trees[it][x] }

            val d1 = viewingDistance(treeHeight, row, x + 1 until width)
            val d2 = viewingDistance(treeHeight, row, x - 1 downTo 0)
            val d3 = viewingDistance(treeHeight, col, y + 1 until height)
            val d4 = viewingDistance(treeHeight, col, y - 1 downTo 0)

            val scenicScore = d1 * d2 * d3 * d4

            maxScenicScore = max(maxScenicScore, scenicScore)
        }
    }

    return maxScenicScore
}

