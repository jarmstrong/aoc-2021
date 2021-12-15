private val openCharacters = setOf('[', '{', '(', '<')
private val closeCharacters = setOf(']', '}', ')', '>')

private fun mapClosingToOpeningCharacter(c: Char): Char {
    return when (c) {
        ']' -> '['
        '}' -> '{'
        ')' -> '('
        '>' -> '<'
        else -> throw Exception("$c not closing character")
    }
}

private fun lineCorruptedAtCharacter(line: String): Char? {
    val lastOpenQueue = ArrayDeque<Char>()
    line.forEach { c ->
        if (c in openCharacters) {
            lastOpenQueue.add(c)
        } else if (c in closeCharacters) {
            if (lastOpenQueue.last() != mapClosingToOpeningCharacter(c)) {
                return c
            } else {
                lastOpenQueue.removeLast()
            }
        }
    }
    return null
}

fun main() {
    fun part1(input: List<String>): Int {
        fun scoreClosingCharacter(c: Char): Int {
            return when (c) {
                ')' -> 3
                ']' -> 57
                '}' -> 1197
                '>' -> 25137
                else -> throw Exception("$c not closing character")
            }
        }

        return input.sumOf { line -> lineCorruptedAtCharacter(line)?.let { scoreClosingCharacter(it) } ?: 0 }
    }

    fun part2(input: List<String>): Long {
        fun scoreClosingCharacter(c: Char): Long {
            return when (c) {
                ')' -> 1
                ']' -> 2
                '}' -> 3
                '>' -> 4
                else -> throw Exception("$c not closing character")
            }
        }

        fun mapOpeningToClosingCharacter(c: Char): Char {
            return when (c) {
                '[' -> ']'
                '{' -> '}'
                '(' -> ')'
                '<' -> '>'
                else -> throw Exception("$c not opening character")
            }
        }

        val scores = input
            .filter { lineCorruptedAtCharacter(it) == null }
            .map { line ->
                val lastOpenQueue = ArrayDeque<Char>()
                line.forEach { c ->
                    if (c in openCharacters) {
                        lastOpenQueue.add(c)
                    } else if (c in closeCharacters) {
                        lastOpenQueue.removeLast()
                    }
                }

                lastOpenQueue
                    .asReversed()
                    .map { mapOpeningToClosingCharacter(it) }
                    .fold(0L) { acc, c -> acc * 5 + scoreClosingCharacter(c) }
            }
            .sorted()

        return scores[scores.size / 2]
    }

    val testInput = readInput("Day10_test")
    check(part1(testInput) == 26397)
    check(part2(testInput) == 288957L)

    val input = readInput("Day10")
    println(part1(input))
    println(part2(input))
}
