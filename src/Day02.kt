private sealed class Command {
    data class Forward(val value: Int) : Command()
    data class Up(val value: Int) : Command()
    data class Down(val value: Int) : Command()
}

private data class Position(val x: Int, val depth: Int, val aim: Int)

private fun Position.multiply(): Int = x * depth

private fun List<String>.toCommands(): List<Command> {
    return map {
        val commandValues = it.split(' ')
        val command = commandValues.first()
        val value = commandValues.last().toInt()

        when (command) {
            "forward" -> Command.Forward(value)
            "up" -> Command.Up(value)
            "down" -> Command.Down(value)
            else -> throw Exception("No command")
        }
    }
}

fun main() {
    fun part1(input: List<Command>): Int {
        return input
            .fold(Position(0, 0, 0)) { position, command ->
                when (command) {
                    is Command.Forward -> position.copy(x = position.x + command.value)
                    is Command.Up -> position.copy(depth = position.depth - command.value)
                    is Command.Down -> position.copy(depth = position.depth + command.value)
                }
            }
            .multiply()
    }

    fun part2(input: List<Command>): Int {
        return input
            .fold(Position(0, 0, 0)) { position, command ->
                when (command) {
                    is Command.Forward -> position.copy(
                        x = position.x + command.value,
                        depth = position.depth + position.aim * command.value
                    )
                    is Command.Up -> position.copy(aim = position.aim - command.value)
                    is Command.Down -> position.copy(aim = position.aim + command.value)
                }
            }
            .multiply()
    }

    val testInput = readInput("Day02_test").toCommands()
    check(part1(testInput) == 150)

    val input = readInput("Day02").toCommands()
    println(part1(input))
    println(part2(input))
}
