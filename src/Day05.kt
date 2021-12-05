import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.min

private data class Point(val x: Int, val y: Int)
private data class LineSegment(val start: Point, val end: Point)

private fun List<List<Int>>.print() {
    forEach { row ->
        row.forEach { if (it == 0) print('.') else print(it) }
        println()
    }
}

private fun createLineSegments(input: List<String>): List<LineSegment> {
    return input.map { lineSegmentString ->
        lineSegmentString
            .split(" -> ")
            .map { it.split(',') }
            .map { Point(it.first().toInt(), it.last().toInt()) }
            .let { LineSegment(it.first(), it.last()) }
    }
}

private fun createEmptyDiagram(input: List<LineSegment>): List<List<Int>> {
    val maxX = input.maxOf { max(it.start.x, it.end.x) }
    val maxY = input.maxOf { max(it.start.y, it.end.y) }

    return (0..maxY).map {
        (0..maxX).map {
            0
        }
    }
}

private fun updateDiagram(points: List<Point>, diagram: List<List<Int>>): List<List<Int>> {
    val mutableDiagram = diagram.map { it.toMutableList() }
    points.forEach {
        val currentValue = mutableDiagram[it.y][it.x]
        mutableDiagram[it.y][it.x] = currentValue + 1
    }
    return mutableDiagram
}

private fun drawLinesToDiagram(input: List<LineSegment>, includeDiagonal: Boolean): List<List<Int>> {
    val emptyDiagram = createEmptyDiagram(input)

    return input.fold(emptyDiagram) { diagram, lineSegment ->
        val (start, end) = lineSegment

        if (start.x != end.x && start.y != end.y && !includeDiagonal) {
            return@fold diagram
        }

        val linePoints = if (start.x != end.x && start.y != end.y) {
            val minX = min(start.x, end.x)
            val maxX = max(start.x, end.x)
            val steps = (maxX - minX).absoluteValue
            (0..steps).map {
                val x = if (start.x < end.x) start.x + it else start.x - it
                val y = if (start.y < end.y) start.y + it else start.y - it
                Point(x, y)
            }
        } else {
            val columnIterator = (min(start.x, end.x)..max(start.x, end.x))
            val rowIterator = (min(start.y, end.y)..max(start.y, end.y))

            columnIterator.flatMap { columnIdx ->
                rowIterator.map { rowIdx ->
                    Point(columnIdx, rowIdx)
                }
            }
        }

        updateDiagram(linePoints, diagram)
    }
}

fun sumOverlappedPoints(completeDiagram: List<List<Int>>): Int {
    return completeDiagram.sumOf { row -> row.count { it >= 2 } }
}

fun main() {
    fun part1(input: List<LineSegment>): Int {
        val completeDiagram = drawLinesToDiagram(input, includeDiagonal = false)
        return completeDiagram.sumOf { row -> row.count { it >= 2 } }
    }

    fun part2(input: List<LineSegment>): Int {
        val completeDiagram = drawLinesToDiagram(input, includeDiagonal = true)
        return sumOverlappedPoints(completeDiagram)
    }

    val testInput = createLineSegments(readInput("Day05_test"))
    val realInput = createLineSegments(readInput("Day05"))

    check(part1(testInput) == 5)
    println(part1(realInput))

    check(part2(testInput) == 12)
    println(part2(realInput))
}
