import beal.Beal

fun main(args: Array<String>) {
    val beal = Beal(
        baseCondition = { it <= 2000 },
        baseCountRange = (1..10),
        baseExponentRange = (1..20),
        exponentRange = (3..7),
    )

    repeat(100000000) {
        println("------\n$it")
        beal.randomTry()
    }
}
