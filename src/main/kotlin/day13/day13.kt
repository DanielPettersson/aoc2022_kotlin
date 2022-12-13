package day13

import kotlinx.serialization.json.*
import java.lang.IllegalArgumentException
import kotlin.math.max

interface Packet {
    fun asListPacket(): ListPacket =
        if (this is ListPacket) this else ListPacket(listOf(this))
}
data class NumPacket(val num: Int): Packet
data class ListPacket(val sub: List<Packet>): Packet

fun parsePacket(line: String): Packet =
    when (val el = Json.parseToJsonElement(line)) {
        is JsonPrimitive -> NumPacket(el.int)
        is JsonArray -> ListPacket(el.map { parsePacket(it.toString()) })
        else -> throw IllegalArgumentException("Illegal packet: $line")
    }

fun isPacketPairInOrder(first: Packet, second: Packet): Boolean? {
    if (first is NumPacket && second is NumPacket) {
        if (first.num < second.num) return true
        if (first.num > second.num) return false
        return null
    }
    val listFirst = first.asListPacket()
    val listSecond = second.asListPacket()
    val len = max(listFirst.sub.size, listSecond.sub.size)
    for (i in 0 until len) {
        if (i > listFirst.sub.lastIndex) return true
        if (i > listSecond.sub.lastIndex) return false
        val maybeOrder = isPacketPairInOrder(listFirst.sub[i], listSecond.sub[i])
        if (maybeOrder != null) return maybeOrder
    }
    return null
}

fun part1(lines: List<String>): Any {
    return lines.chunked(3).mapIndexed { i, linesChunk ->
        val isInOrder = isPacketPairInOrder(
            parsePacket(linesChunk[0]),
            parsePacket(linesChunk[1])
        ) ?: throw IllegalArgumentException("Undecided packet pair: $linesChunk")
        if (isInOrder) i + 1 else 0
    }.sum()
}

fun part2(lines: List<String>): Any {
    val divider1 = parsePacket("[[2]]")
    val divider2 = parsePacket("[[6]]")
    val packets = lines.filter { it.isNotBlank() }
        .map { parsePacket(it) } + listOf(divider1, divider2)
    val sortedPackets = packets.sortedWith { a, b ->
        when (isPacketPairInOrder(a, b)) {
            true -> -1
            false -> 1
            else -> 0
        }
    }
    val i1 = sortedPackets.indexOf(divider1) + 1
    val i2 = sortedPackets.indexOf(divider2) + 1
    return i1 * i2
}