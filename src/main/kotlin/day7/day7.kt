@file:Suppress("unused")

package day7

class FsNode(
    val parent: FsNode?,
    val name: String,
    val fileSize: Int = 0,
    var children: List<FsNode> = listOf()
) {
    fun cd(folder: String): FsNode =
    when (folder) {
        ".." -> parent ?: throw RuntimeException("Folder ${name} has no parent")
        "/" -> when (parent) {
            null -> this
            else -> parent.cd("/")
        }
        else -> children.first { !it.isFile() && it.name == folder }
    }
    fun size(): Int = children.sumOf { it.size() } + fileSize
    fun isFile(): Boolean = fileSize > 0
}

data class CmdResult(val fsNode: FsNode, val index: Int)

fun processCmd(i: Int, lines: List<String>, current: FsNode): CmdResult {
    val cmd = lines[i]
    if (cmd.startsWith("$ cd ")) {
        return CmdResult(
            current.cd(cmd.substringAfter("$ cd ")),
            i + 1
        )
    } else if (cmd.startsWith("$ ls")) {
        current.children = lines.subList(i + 1, lines.lastIndex)
            .takeWhile { !it.startsWith("$") }
            .map {
                val parts = it.split(" ")
                when (parts[0]) {
                    "dir" -> FsNode(current, parts[1])
                    else -> FsNode(current, parts[1], fileSize = parts[0].toInt())
                }
            }
        return CmdResult(
            current,
            i + 1 + current.children.size
        )
    }
    throw RuntimeException("Unknown command: $cmd")
}

fun searchTree(fsNode: FsNode, matcher: (FsNode) -> Boolean): List<FsNode> {
    val ret = mutableListOf<FsNode>()
    if (matcher(fsNode)) {
        ret.add(fsNode)
    }
    fsNode.children.forEach {
        ret.addAll(searchTree(it, matcher))
    }
    return ret
}

fun createTreeFromCmds(lines: List<String>): FsNode {
    var current = FsNode(null, "")
    var currentIndex = 1

    while (currentIndex < lines.lastIndex) {
        val (folder, index) = processCmd(currentIndex, lines, current)
        current = folder
        currentIndex = index
    }
    return current.cd("/")
}

fun part1(lines: List<String>): Int {
    val root = createTreeFromCmds(lines)
    return searchTree(root) { !it.isFile() && it.size() <= 100000 }.sumOf { it.size() }
}

fun part2(lines: List<String>): Int {
    val totalSpace = 70_000_000
    val neededUnusedSpace = 30_000_000

    val root = createTreeFromCmds(lines)
    val usedSpace = root.size()
    val spaceToCleanUp = neededUnusedSpace - totalSpace + usedSpace

    return searchTree(root) { !it.isFile() && it.size() > spaceToCleanUp }
        .sortedBy { it.size() }
        .map { it.size() }
        .first()

}
