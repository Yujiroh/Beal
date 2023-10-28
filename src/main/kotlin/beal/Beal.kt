package beal

import beal.ext.toBigInteger
import java.math.BigInteger
import kotlin.system.exitProcess

class Beal(
    private val baseCondition: (Int) -> Boolean,
    private val baseCountRange: IntRange,
    private val baseExponentRange: IntRange,
    private val exponentRange: IntRange,
) {
    companion object {
        fun create(
            bases: Collection<Pair<Int, Int>>,
            exponent: Int
        ): BigInteger {
            val base = bases.map { (base, exponent) -> base.toBigInteger().pow(exponent) }.reduce { acc, bigInteger -> acc * bigInteger }
            val value = base.pow(exponent)
            return value
        }

        fun calc() {
            val beal = Beal(
                baseCondition = { it <= 2000 },
                baseCountRange = (1..9),
                baseExponentRange = (1..4),
                exponentRange = (3..10),
            )

            repeat(10) {
                println("------\n$it")
                beal.randomTry()
            }
        }
    }

    init {
        require(baseCountRange.first >= 1)
        require(baseExponentRange.first >= 1)
        require(exponentRange.first >= 3)
    }

    fun randomTry() {
        val primitive = Primitive()
        val left1 = createLeft(primitive, setOf(true, false).random())
        val left2 = createLeft(primitive, false)

        val left = left1 + left2
        println("Left=$left")

        checkRight(primitive, left)
    }

    private fun createLeft(
        primitive: Primitive,
        includeTwo: Boolean
    ): BigInteger {
        val primitives = primitive.selectLeft(baseCondition, baseCountRange.random(), includeTwo)
        val bases = primitives.map { Pair(it, baseExponentRange.random()) }
        val exponent = exponentRange.random()
        val value = create(bases, exponent)
        println("Left bases=$bases exponent=$exponent\nvalue=$value")
        return value
    }

    private fun checkRight(
        primitive: Primitive,
        left: BigInteger,
    ) {
        val primitives = primitive.selectRight(baseCondition, baseCountRange.random())
        val bases = primitives.map { Pair(it, baseExponentRange.random()) }

        val base = bases.map { (base, exponent) -> base.toBigInteger().pow(exponent) }.reduce { acc, bigInteger -> acc * bigInteger }
        println("Right base=$base")
        if (!Remainder.enable(left, base)) {
            println("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
            return
        }

        (3..Short.MAX_VALUE.toInt()).forEach { exponent ->
            val value = create(bases, exponent)
            if (left == value) {
                println("Goal")
                exitProcess(0)
            } else if (value > left) {
                println("Right bases=$bases exponent=$exponent\nvalue=$value")
                return
            }
        }
    }
}