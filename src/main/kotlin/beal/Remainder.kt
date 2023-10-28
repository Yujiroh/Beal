package beal

import java.math.BigInteger
import kotlin.system.exitProcess

class Remainder(
    val base: Int,
) {
    companion object {
        val makables: Map<Int, Map<Int, Set<Int>>> =
            (1..100).map {
                val remainder = Remainder(it)
                val makable = remainder.calc()
                it to makable
            }.toMap()

        fun enable(
            left: BigInteger,
            rightBase: BigInteger
        ): Boolean =
            makables.all { (key, value) ->
                val leftMod = (left % key.toBigInteger()).toInt()
                val rightMod = (rightBase % key.toBigInteger()).toInt()
                val result = value[leftMod]!!.contains(rightMod)
                if (!result) {
                    println("key=$key leftMod=$leftMod rightMod=$rightMod")
                }
                result
            }
    }

    fun calc(): Map<Int, Set<Int>> {
        println("-------")
        println("base=$base")
        fun calc(baseRemainder: Int): Pair<Int, List<Int>> {
            val remainders = mutableListOf<Int>()
            var current = baseRemainder
            while (current !in remainders) {
                remainders.add(current)
                current = current * baseRemainder % base
            }
            remainders.add(current)
            return Pair(baseRemainder, remainders.toList())
        }

        val remainderMap = mutableMapOf<Int, List<Int>>()
        repeat(base) { remainder ->
            val (_, remainders) = calc(remainder)
            println("${remainder}: ${remainders.joinToString(",")}")
            remainderMap[remainder] = remainders
        }

        val makables = (0..base - 1).map {
            val value = remainderMap.entries.fold(mutableSetOf<Int>()) { sum, (key, list) ->
                val fromIndex = Math.min(list.indexOf(list.last()), 2)
                val sublist = list.subList(fromIndex, list.size)
                if (it in sublist) {
                    sum.add(key)
                }
                sum
            }
            it to value.toSet()
        }.toMap()

        repeat(base) { remainder ->
            println("${remainder}: ${makables[remainder]}")
        }
        return makables
    }
}