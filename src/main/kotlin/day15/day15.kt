package day15

import kotlin.math.abs

data class Pos(val x: Long, val y: Long) {
    fun distanceTo(pos: Pos): Long = abs(x - pos.x) + abs(y - pos.y)
}

data class Beacon(val pos: Pos)

data class Sensor(val pos: Pos, val closestBeacon: Beacon) {
    val distanceToBeacon: Long by lazy { pos.distanceTo(closestBeacon.pos) }
    fun isBeaconPossible(p: Pos): Boolean = isOtherBeaconPossible(p) || closestBeacon.pos == p
    fun isOtherBeaconPossible(p: Pos): Boolean = distanceToBeacon < pos.distanceTo(p)
    fun nextPossibleX(p: Pos): Long {
        val dy = abs(p.y - pos.y)
        return pos.x + distanceToBeacon - dy + 1
    }
}

val sensorRegex = Regex("Sensor at x=(-*\\d+), y=(-*\\d+): closest beacon is at x=(-*\\d+), y=(-*\\d+)")

fun parseSensor(line: String): Sensor {
    val match = sensorRegex.find(line) ?: throw IllegalArgumentException("Illegal sensor line: $line")
    val (sx, sy, bx, by) = match.destructured
    return Sensor(Pos(sx.toLong(), sy.toLong()), Beacon(Pos(bx.toLong(), by.toLong())))
}

fun part1(lines: List<String>): Any {
    val sensors = lines.filter { it.isNotBlank() }.map { parseSensor(it) }
    val y = 2000000L
    val minX = sensors.minOf { it.pos.x - it.distanceToBeacon }
    val maxX = sensors.maxOf { it.pos.x + it.distanceToBeacon }
    return (minX..maxX).map { Pos(it, y) }.count { pos -> sensors.any { !it.isBeaconPossible(pos) } }
}

fun part2(lines: List<String>): Any {
    val sensors = lines.filter { it.isNotBlank() }.map { parseSensor(it) }
    for (y in 0..4_000_000L) {
        var x = 0L
        while (x <= 4_000_000L) {
            val p = Pos(x, y)
            val notPossibleSensors = sensors.filter { !it.isOtherBeaconPossible(p) }
            if (notPossibleSensors.isEmpty()) {
                return p.x * 4_000_000 + p.y
            }
            x = notPossibleSensors.maxOf { it.nextPossibleX(p) }

        }
    }
    return 0
}