data class PuzzleInput(val signalPatterns: List<String>, val outputValue: List<String>)

fun readPuzzleInput(filename: String): List<PuzzleInput> {
    return readInput(filename)
        .map {
            val lineSplit = it.split(" | ")

            PuzzleInput(
                signalPatterns = lineSplit.first().split(' '),
                outputValue = lineSplit.last().split(' ')
            )
        }
}

fun main() {
    fun part1(input: List<PuzzleInput>): Int {
        // 1, 4, 7, 8
        val digitsWithUniqueSegmentCount = setOf(2, 4, 3, 7)

        return input
            .flatMap { it.outputValue }
            .count { it.length in digitsWithUniqueSegmentCount }
    }

    fun part2(input: List<PuzzleInput>): Int {
        return input
            .map { puzzleInput ->
                fun findDigitPatternBySegmentCount(segments: Int) =
                    puzzleInput.signalPatterns.first { it.length == segments }.toSortedSet()

                fun findDigitPatternsBySegmentCount(segments: Int) =
                    puzzleInput.signalPatterns.filter { it.length == segments }.map { it.toSortedSet() }

                val oneSegment = findDigitPatternBySegmentCount(2)
                val fourSegment = findDigitPatternBySegmentCount(4)
                val sevenSegment = findDigitPatternBySegmentCount(3)
                val eightSegment = findDigitPatternBySegmentCount(7)

                val sixSegmentPatterns = findDigitPatternsBySegmentCount(6)
                val sixSegment = sixSegmentPatterns
                    .first { segment -> segment.count { it in oneSegment } == 1 }
                    .toSortedSet()
                val nineSegment = sixSegmentPatterns
                    .filter { it != sixSegment }
                    .single { segment -> segment.containsAll(oneSegment) && segment.containsAll(fourSegment) }
                val zeroSegment = sixSegmentPatterns.first { it != sixSegment && it != nineSegment }

                val topRightLetter = oneSegment.first { it !in sixSegment }
                val middleLetter = (eightSegment - zeroSegment).single()
                val bottomLeftLetter = (eightSegment - nineSegment).single()
                val bottomRightLetter = oneSegment.first { it in sixSegment }
                val topLeftLetter = (fourSegment - middleLetter - oneSegment).single()

                val twoSegment = (eightSegment - topLeftLetter - bottomRightLetter).toSortedSet()
                val threeSegment = (eightSegment - topLeftLetter - bottomLeftLetter).toSortedSet()
                val fiveSegment = (eightSegment - topRightLetter - bottomLeftLetter).toSortedSet()

                puzzleInput.outputValue
                    .map { it.toSortedSet() }
                    .map {
                        when (it) {
                            zeroSegment -> 0
                            oneSegment -> 1
                            twoSegment -> 2
                            threeSegment -> 3
                            fourSegment -> 4
                            fiveSegment -> 5
                            sixSegment -> 6
                            sevenSegment -> 7
                            eightSegment -> 8
                            nineSegment -> 9
                            else -> throw Exception("Not good")
                        }
                    }
                    .joinToString(separator = "")
            }
            .sumOf { it.toInt() }
    }

    val testInput = readPuzzleInput("Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readPuzzleInput("Day08")
    println(part1(input))
    println(part2(input))
}
