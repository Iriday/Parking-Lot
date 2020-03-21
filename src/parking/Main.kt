package parking

import java.util.*

fun main() {
    Main().run()
}

class Main {
    private val scn = Scanner(System.`in`)
    private val spots = arrayOf(Spot("1234567890", "Red"), null, null)

    fun run() {
        var on = true

        while (on) {
            val input = input()

            when (input[0].toLowerCase()) {
                "park" -> {
                    val spotNumber = park(input[1], input[2], spots)
                    if (spotNumber == -1) output("No empty spots left.")
                    else output("${input[2]} car parked on the spot $spotNumber.")
                }
                "leave" -> {
                    val leftOrNot = leave(input[1].toInt(), spots)
                    if (leftOrNot) output("Spot ${input[1]} is free.")
                    else output("There is no car in the spot ${input[1]}.")
                }
                "exit" -> on = false

                else -> output("Incorrect input.")
            }
        }
    }

    private fun input(): List<String> = scn.nextLine().trim().split(Regex(" "))

    private fun output(str: String) = println(str)
}

fun park(unitNumber: String, unitColor: String, spots: Array<Spot?>): Int {
    val emptySpotIndex = if (spots[0] == null) 0 else if (spots[1] == null) 1 else if (spots[2] == null) 2 else -1

    if (emptySpotIndex == -1) return emptySpotIndex

    spots[emptySpotIndex] = Spot(unitNumber, unitColor)
    return emptySpotIndex + 1
}

fun leave(spotNumber: Int, spots: Array<Spot?>): Boolean {
    if (spots[spotNumber - 1] == null) return false
    spots[spotNumber - 1] = null
    return true
}
