private typealias BingoBoard = List<List<BingoNumber>>
private data class BingoNumber(val value: Int, val marked: Boolean)
private data class BingoSimulation(val bingoNumberDraws: List<Int>, val boards: List<BingoBoard>)

fun main() {
    fun parseBingoBoard(boardStrings: List<String>): BingoBoard {
        return boardStrings.map { row -> row.chunked(3).map { BingoNumber(it.trim().toInt(), false) } }
    }

    fun parseBingoSimulation(input: List<String>): BingoSimulation {
        val bingoNumberDraws = input.first().split(',').map { it.toInt() }
        val bingoBoards = input
            .drop(1)
            .filter { it.isNotEmpty() }
            .chunked(5)
            .map { parseBingoBoard(it) }

        return BingoSimulation(bingoNumberDraws, bingoBoards)
    }

    fun checkSolved(bingoBoard: BingoBoard): Boolean {
        val hasMarkedRow = bingoBoard.any { row ->
            row.count { number -> number.marked } == 5
        }
        if (hasMarkedRow) return true

        return (0..4).any { row ->
            (0..4).count { column -> bingoBoard[column][row].marked } == 5
        }
    }

    fun sumUnmarkedNumbers(bingoBoard: BingoBoard): Int {
        return bingoBoard
            .flatMap { it.filter { number -> !number.marked } }
            .sumOf { number -> number.value }
    }

    fun updateBoard(drawnBingoNumber: Int, bingoBoard: BingoBoard): BingoBoard {
        return bingoBoard.map { row ->
            row.map { bingoNumber -> if (bingoNumber.value == drawnBingoNumber) bingoNumber.copy(marked = true) else bingoNumber }
        }
    }

    fun part1(input: BingoSimulation): Int {
        input.bingoNumberDraws.fold(input.boards) { boards, drawnBingoNumber ->
            boards
                .map { bingoBoard -> updateBoard(drawnBingoNumber, bingoBoard) }
                .onEach { bingoBoard ->
                    if (checkSolved(bingoBoard)) {
                        return drawnBingoNumber * sumUnmarkedNumbers(bingoBoard)
                    }
                }
        }

        throw Exception("Solution not found")
    }

    fun part2(input: BingoSimulation): Int {
        input.bingoNumberDraws.fold(Pair(input.boards, listOf<BingoBoard>())) { (boards, winners), drawnBingoNumber ->
            val updatedBoards = (boards - winners).map { updateBoard(drawnBingoNumber, it) }

            val newBoardWinners = updatedBoards.filter {
                val isBoardSolved = checkSolved(it)
                if (isBoardSolved && winners.size + 1 == input.boards.size) {
                    return drawnBingoNumber * sumUnmarkedNumbers(it)
                }
                isBoardSolved
            }

            Pair(updatedBoards, newBoardWinners + winners)
        }

        throw Exception("Didn't find last winning board")
    }

    val testInput = parseBingoSimulation(readInput("Day04_test"))
    val realInput = parseBingoSimulation(readInput("Day04"))

    check(part1(testInput) == 4512)
    println(part1(realInput))

    check(part2(testInput) == 1924)
    println(part2(realInput))
}
