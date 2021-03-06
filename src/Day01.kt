fun main() {
    fun part1(input: List<Int>): Int {
        return input
            .zipWithNext()
            .count { it.second > it.first }
    }

    fun part2(input: List<Int>): Int {
        val sums = input.windowed(3) { it.sum() }
        return part1(sums)
    }

    val testInput = readInput("Day01_test").map { it.toInt() }
    check(part1(testInput) == 7)

    val input = readInput("Day01").map { it.toInt() }
    println(part1(input))
    println(part2(input))
}
