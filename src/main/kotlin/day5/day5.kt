@file:Suppress("unused")
package day5

fun initStacks(lines: List<String>): List<ArrayDeque<String>> {
    val stacks = mutableListOf<ArrayDeque<String>>()

    lines.takeWhile { it.isNotBlank() }.forEach {
        it.chunked(4).forEachIndexed {i, chunk ->
            if (stacks.size <= i) {
                stacks.add(ArrayDeque())
            }
            if (chunk.contains("[")) {
                stacks[i].addFirst(chunk[1].toString())
            }
        }
    }

    return stacks
}

data class Move(val quantity: Int, val fromIndex: Int, val toIndex: Int)

val moveRegex = Regex("move (\\d+) from (\\d+) to (\\d+)")

fun initMoves(lines: List<String>): List<Move> {
    val separatorLine = lines.indexOf("")
    return lines.subList(separatorLine + 1, lines.lastIndex).map {
        val match = moveRegex.find(it) ?: throw IllegalArgumentException("Illegal move line: $it")
        val (q, f, t) = match.destructured
        Move(q.toInt(), f.toInt() - 1, t.toInt() - 1)
    }
}

fun applyMove(stacks: List<ArrayDeque<String>>, move: Move) {
    repeat(move.quantity) {
        val crate = stacks[move.fromIndex].removeLast()
        stacks[move.toIndex].addLast(crate)
    }
}

fun applyMultiMove(stacks: List<ArrayDeque<String>>, move: Move) {
    val crates = stacks[move.fromIndex].takeLast(move.quantity)
    crates.forEach {
        stacks[move.fromIndex].removeLast()
        stacks[move.toIndex].addLast(it)
    }
}

fun part1(lines: List<String>): Any {
    val stacks = initStacks(lines)
    val moves = initMoves(lines)
    moves.forEach { applyMove(stacks, it) }
    return stacks.joinToString(separator = "") { it.last() }
}

fun part2(lines: List<String>): Any {
    val stacks = initStacks(lines)
    val moves = initMoves(lines)
    moves.forEach { applyMultiMove(stacks, it) }
    return stacks.joinToString(separator = "") { it.last() }
}