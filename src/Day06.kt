private fun parseInput(input: List<String>): List<Int> {
    return input
        .first()
        .split(",")
        .map { it.toInt() }
}

private fun List<Int>.numberOfFishAfter(days: Int): Long {
    val fishMap = groupingBy { it }
        .eachCount()
        .mapValues { it.value.toLong() }
        .toMutableMap()

    (0 until days).forEach { _ ->
        (0..8).forEach {
            fishMap[it - 1] = fishMap.getOrDefault(it, 0)
        }

        val fishToRespawn = fishMap.getOrDefault(-1, 0)
        fishMap.remove(-1)
        fishMap[6] = fishMap.getOrDefault(6, 0) + fishToRespawn
        fishMap[8] = fishToRespawn
    }

    return fishMap.values.sum()
}

fun main() {
    fun part1(input: List<Int>): Long {
        return input.numberOfFishAfter(80)
    }

    fun part2(input: List<Int>): Long {
        return input.numberOfFishAfter(256)
    }

    val testInput = parseInput(readInput("Day06_test"))
    val realInput = parseInput(readInput("Day06"))

    check(part1(testInput) == 5934L)
    println(part1(realInput))

    check(part2(testInput) == 26984457539L)
    println(part2(realInput))
}
