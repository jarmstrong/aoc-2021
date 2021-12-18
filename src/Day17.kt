import kotlin.math.absoluteValue
import kotlin.math.max

private data class Input(val xMin: Int, val xMax: Int, val yMin: Int, val yMax: Int)

private fun input(name: String): Input {
    val ranges = readInput(name)
        .first()
        .removePrefix("target area: ")
        .split(", ")
        .map {
            val range = it.drop(2).split("..")
            Pair(range.first().toInt(), range.last().toInt())
        }
    val xRange = ranges.first()
    val yRange = ranges.last()
    return Input(xRange.first, xRange.second, yRange.first, yRange.second)
}

private fun Input.insideTargetArea(x: Int, y: Int): Boolean {
    return x in xMin..xMax && y in yMin..yMax
}

private fun Input.willNeverBeInsideTargetArea(x: Int, y: Int): Boolean {
    return x > xMax || y < yMin
}

private data class ProbeResult(val initialVelX: Int, val initialVelY: Int, val maxY: Int)

private fun fireProbe(input: Input, initialVelX: Int, initialVelY: Int): ProbeResult? {
    var x = 0
    var y = 0
    var xVel = initialVelX
    var yVel = initialVelY
    var maxY = y

    while (!input.insideTargetArea(x, y) && !input.willNeverBeInsideTargetArea(x, y)) {
        x += xVel
        y += yVel
        maxY = max(maxY, y)
        if (xVel > 0) xVel--
        yVel--

    }

    return if (input.insideTargetArea(x, y)) {
        ProbeResult(initialVelX, initialVelY, maxY)
    } else {
        null
    }
}

private fun collectProbeResults(input: Input): List<ProbeResult> {
    return (0..input.xMax).flatMap { xVel ->
        (input.yMin..input.yMin.absoluteValue).mapNotNull { yVel ->
            fireProbe(input, xVel, yVel)
        }
    }
}

fun main() {
    fun part1(input: Input): Int {
        return collectProbeResults(input).maxOf { it.maxY }
    }

    fun part2(input: Input): Int {
        return collectProbeResults(input).count()
    }

    val testInput = input("Day17_test")
    check(part1(testInput) == 45)
    check(part2(testInput) == 112)

    val input = input("Day17")
    println(part1(input))
    println(part2(input))
}
