import kotlin.math.absoluteValue

private fun readCrabPositions(filename: String): List<Int> {
    return readInput(filename).first().split(',').map { it.toInt() }
}

fun calculateMinimumFuelCost(input: List<Int>, fuelCostToTarget: (Int, Int) -> Int): Int {
    val maxCrabPosition = input.maxOf { it }

    return (0..maxCrabPosition).minOf { targetCrabPosition ->
        input.sumOf { currentCrabPosition -> fuelCostToTarget(currentCrabPosition, targetCrabPosition) }
    }
}

fun main() {
    fun part1(input: List<Int>): Int {
        return calculateMinimumFuelCost(input) { currentCrabPosition, targetCrabPosition ->
            (currentCrabPosition - targetCrabPosition).absoluteValue
        }
    }

    fun part2(input: List<Int>): Int {
        return calculateMinimumFuelCost(input) { currentCrabPosition, targetCrabPosition ->
            val distanceToTargetCragPosition = (currentCrabPosition - targetCrabPosition).absoluteValue
            distanceToTargetCragPosition * (distanceToTargetCragPosition + 1) / 2
        }
    }

    val testInput = readCrabPositions("Day07_test")
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readCrabPositions("Day07")
    println(part1(input))
    println(part2(input))
}
