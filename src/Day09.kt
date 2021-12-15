private typealias HeightMap = MutableMap<Int, MutableMap<Int, Int>>

private fun readHeightMap(name: String): HeightMap {
    val rows = readInput(name)
        .map { it.map { char -> char.digitToInt() } }

    val heightMap = mutableMapOf<Int, MutableMap<Int, Int>>()

    rows.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { columnIndex, value ->
            val heightMapRow = heightMap.getOrDefault(rowIndex, mutableMapOf())
            heightMapRow[columnIndex] = value
            heightMap[rowIndex] = heightMapRow
        }
    }

    return heightMap
}

private fun HeightMap.getValue(rowIndex: Int, columnIndex: Int): Int = getValue(rowIndex).getValue(columnIndex)

private fun HeightMap.indexExists(rowIndex: Int, columnIndex: Int) =
    containsKey(rowIndex) && getValue(rowIndex)[columnIndex] != null

private fun HeightMap.isLowPoint(rowIndex: Int, columnIndex: Int): Boolean {
    val height = getValue(rowIndex, columnIndex)

    val upHeight = if (indexExists(rowIndex - 1, columnIndex)) getValue(rowIndex - 1, columnIndex) else 10
    val downHeight = if (indexExists(rowIndex + 1, columnIndex)) getValue(rowIndex + 1, columnIndex) else 10
    val leftHeight = if (indexExists(rowIndex, columnIndex - 1)) getValue(rowIndex, columnIndex - 1) else 10
    val rightHeight = if (indexExists(rowIndex, columnIndex + 1)) getValue(rowIndex, columnIndex + 1) else 10

    return height < upHeight
            && height < downHeight
            && height < leftHeight
            && height < rightHeight
}

private fun HeightMap.getLowPoints(): List<Pair<Int, Int>> {
    return (0 until keys.size).flatMap { rowIndex ->
        (0 until getValue(rowIndex).size).mapNotNull { columnIndex ->
            if (isLowPoint(rowIndex, columnIndex)) {
                Pair(rowIndex, columnIndex)
            } else {
                null
            }
        }
    }
}

fun main() {
    fun part1(input: HeightMap): Int {
        fun HeightMap.riskLevel(rowIndex: Int, columnIndex: Int) =
            1 + getValue(rowIndex).getValue(columnIndex)

        return input
            .getLowPoints()
            .sumOf { input.riskLevel(it.first, it.second) }
    }

    fun getBasinPoints(
        heightMap: HeightMap,
        lastHeight: Int,
        rowIndex: Int,
        columnIndex: Int,
        visited: Set<Pair<Int, Int>>
    ): Set<Pair<Int, Int>> {
        val currentPair = Pair(rowIndex, columnIndex)
        if (visited.contains(currentPair) || !heightMap.indexExists(rowIndex, columnIndex)) return setOf()

        val currentHeight = heightMap.getValue(rowIndex, columnIndex)
        if (currentHeight == 9 || currentHeight < lastHeight) return setOf()

        val nextVisited = visited + currentPair

        return setOf(currentPair) +
                getBasinPoints(heightMap, currentHeight, rowIndex - 1, columnIndex, nextVisited) +
                getBasinPoints(heightMap, currentHeight, rowIndex + 1, columnIndex, nextVisited) +
                getBasinPoints(heightMap, currentHeight, rowIndex, columnIndex - 1, nextVisited) +
                getBasinPoints(heightMap, currentHeight, rowIndex, columnIndex + 1, nextVisited)
    }

    fun part2(input: HeightMap): Int {
        return input.getLowPoints()
            .map { getBasinPoints(input, input.getValue(it.first, it.second), it.first, it.second, emptySet()).size }
            .sorted()
            .takeLast(3)
            .reduce { acc, next -> acc * next }
    }

    check(part1(readHeightMap("Day09_test")) == 15)
    check(part2(readHeightMap("Day09_test")) == 1134)

    println(part1(readHeightMap("Day09")))
    println(part2(readHeightMap("Day09")))
}
