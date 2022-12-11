@file:Suppress("unused")

package day11

import kotlin.math.max
import kotlin.math.min

class Monkey(
    var items: MutableList<Long>,
    val operation: (Long) -> Long,
    val testDivisor: Long,
    private val targetTrue: Int,
    private val targetFalse: Int,
    var inspectedItems: Long = 0,
) {
    fun doItems(worryFnc: (Long) -> Long): List<ItemResult> {
        val ret = items.map {
            val l = worryFnc(operation(it))
            ItemResult(
                worryLevel = l,
                targetMonkey = if (l % testDivisor == 0L) targetTrue else targetFalse
            )
        }
        inspectedItems += items.size
        items.clear()
        return ret
    }
}

data class ItemResult(val worryLevel: Long, val targetMonkey: Int)

fun parseMonkey(lines: List<String>): Monkey {
    val items = lines[1].substringAfter("Starting items: ").split(", ").map { it.toLong() }.toMutableList()
    val (op, opVal) = lines[2].substringAfter("Operation: new = old ").split(" ")
    val opFnc: (Long, Long) -> Long = when (op) {
        "*" -> { i1, i2 -> i1 * i2 }
        "+" -> { i1, i2 -> i1 + i2 }
        else -> throw IllegalArgumentException("Illegal operation line: lines[2]")
    }
    val opValFnc: (Long) -> Long = when (opVal) {
        "old" -> { i -> i }
        else -> { _ -> opVal.toLong() }
    }
    val operation: (Long) -> Long = { opFnc(it, opValFnc(it)) }

    val testVal = lines[3].substringAfter("Test: divisible by ").toLong()
    val targetTrue = lines[4].substringAfter("If true: throw to monkey ").toInt()
    val targetFalse = lines[5].substringAfter("If false: throw to monkey ").toInt()

    return Monkey(items, operation, testVal, targetTrue, targetFalse)
}

fun lcm(number1: Long, number2: Long): Long {
    val higherNumber = max(number1, number2)
    val lowerNumber = min(number1, number2)
    var lcm = higherNumber
    while (lcm % lowerNumber != 0L) {
        lcm += higherNumber
    }
    return lcm
}

fun part1(lines: List<String>): Any {
    val monkeys = (0..lines.lastIndex step 7).map { l -> parseMonkey(lines.subList(l, l + 7)) }
    repeat(20) {
        monkeys.forEach { monkey ->
            monkey.doItems { it/3 }.forEach {
                monkeys[it.targetMonkey].items.add(it.worryLevel)
            }
        }
    }
    val sortedInspectedItems = monkeys.sortedByDescending { it.inspectedItems }.map { it.inspectedItems }
    return sortedInspectedItems[0] * sortedInspectedItems[1]
}

fun part2(lines: List<String>): Any {
    val monkeys = (0..lines.lastIndex step 7).map { parseMonkey(lines.subList(it, it + 7)) }
    val lcm = monkeys.fold(1L) {acc, m -> lcm(acc, m.testDivisor) }
    repeat(10000) {
        monkeys.forEach { monkey ->
            monkey.doItems { it % lcm }.forEach {
                monkeys[it.targetMonkey].items.add(it.worryLevel)
            }
        }
    }
    val sortedInspectedItems = monkeys.sortedByDescending { it.inspectedItems }.map { it.inspectedItems }
    return sortedInspectedItems[0] * sortedInspectedItems[1]
}