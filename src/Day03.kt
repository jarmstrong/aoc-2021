fun main() {
    fun part1(input: List<String>): Int {
        val gammaRate = input
            .fold(sortedMapOf<Int, Int>()) { acc, string ->
                string.forEachIndexed { index, c ->
                    val onesCount = acc.getOrDefault(index, 0)
                    if (c == '1') acc[index] = onesCount + 1
                }
                acc
            }
            .map { if (it.value > input.size / 2) 1 else 0 }
            .joinToString(separator = "")

        val decimalGammaRate = gammaRate.toInt(radix = 2)
        val epsilonRate = Integer.toBinaryString(decimalGammaRate.inv()).takeLast(input.first().length)
        return decimalGammaRate * epsilonRate.toInt(radix = 2)
    }

    fun calculateRating(input: List<String>, selectCommonBit: (List<String>, List<String>) -> List<String>): String {
        return (0 until input.first().length)
            .fold(input) { acc, index ->
                if (acc.size == 1) return@fold acc

                val bitGrouping = acc.groupBy { it[index] }
                val zeros = bitGrouping.getOrDefault('0', emptyList())
                val ones = bitGrouping.getOrDefault('1', emptyList())
                selectCommonBit(zeros, ones)
            }
            .first()
    }

    fun part2(input: List<String>): Int {
        val oxygenGeneratorRating = calculateRating(input) { zeros, ones -> if (ones.size >= zeros.size) ones else zeros }
        val scrubberRating = calculateRating(input) { zeros, ones -> if (zeros.size <= ones.size) zeros else ones }
        return oxygenGeneratorRating.toInt(radix = 2) * scrubberRating.toInt(radix = 2)
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198)
    check(part2(testInput) == 230)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}
