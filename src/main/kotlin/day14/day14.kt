package day14

data class Pos(val x: Int, val y: Int)

class ParseResult(val source: Pos, val world: Array<CharArray>)

private infix fun Int.toward(to: Int): IntProgression {
    val step = if (this > to) -1 else 1
    return IntProgression.fromClosedRange(this, to, step)
}

fun parseWorld(lines: List<String>): ParseResult {
    val poss = lines.filter { it.isNotBlank() }.map { line ->
        line.split(" -> ").map {
            val (x, y) = it.split(",")
            Pos(x.toInt(), y.toInt())
        }
    }
    val minX = poss.flatten().minOf { it.x } - 1
    val maxX = poss.flatten().maxOf { it.x } + 1
    val minY = 0
    val maxY = poss.flatten().maxOf { it.y } + 1

    val world = (minY..maxY).map { CharArray(maxX - minX + 1) {'.'} }.toTypedArray()

    poss.forEach { p ->
        for (i in 0 until p.lastIndex) {
            val p1 = p[i]
            val p2 = p[i + 1]
            if (p1.x == p2.x) {
                for (y in p1.y toward p2.y) {
                    world[y - minY][p1.x - minX] = '#'
                }
            } else {
                for (x in p1.x toward p2.x) {
                    world[p1.y - minY][x - minX] = '#'
                }
            }
        }
    }

    return ParseResult(Pos(500 - minX, 0), world)
}

fun printWorld(world: Array<CharArray>, source: Pos) {
    world.forEachIndexed { y, l ->
        l.forEachIndexed { x, c ->
            if (source == Pos(x, y)) print("+")
            else print(c)
        }
        print("\n")
    }
}

fun getMove(world: Array<CharArray>, p: Pos): Pos? {
    if (p.y >= world.lastIndex - 1) return p.copy(y=p.y + 1)
    if (world[p.y + 1][p.x] == '.') return p.copy(y=p.y + 1)
    if (world[p.y + 1][p.x - 1] == '.') return Pos(p.x - 1, p.y + 1)
    if (world[p.y + 1][p.x + 1] == '.') return Pos(p.x + 1, p.y + 1)
    return null
}

fun doSimulation(res: ParseResult): Array<CharArray> {
    var sandPos = res.source

    while (sandPos.y < res.world.size) {
        sandPos = res.source
        do {
            val newSandPos = getMove(res.world, sandPos)
            if (newSandPos != null) {
                sandPos = newSandPos
            }
        } while (newSandPos != null && newSandPos.y < res.world.size)
        if (sandPos.y < res.world.size) res.world[sandPos.y][sandPos.x] = 'o'
    }

    return res.world
}

fun part1(lines: List<String>): Any {
    val res = parseWorld(lines)
    val world = doSimulation(res)
    printWorld(world, res.source)

    return res.world.map { l -> l.count { it == 'o' } }.sum()
}