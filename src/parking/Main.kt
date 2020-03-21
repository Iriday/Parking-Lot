package parking

import java.util.*

fun main() {
    Main().run()
}

class Main {
    private val scn = Scanner(System.`in`)
    private lateinit var spots: Array<Spot?>

    fun run() {
        var on = true
        var created = false // parking lot

        while (on) {
            val input = input()

            when (input[0].toLowerCase()) {
                "create" -> {
                    val size = input[1].toInt()
                    if (size < 1) output("Incorrect input.")
                    else {
                        spots = Array(size) { null }
                        output("Created a parking lot with $size spots.")
                        created = true
                    }
                }
                "park" -> {
                    if (!created) output("Parking lot is not created.")
                    else {
                        val spotNumber = park(input[1], input[2], spots)
                        if (spotNumber == -1) output("Sorry, parking lot is full.")
                        else output("${input[2]} car parked on the spot $spotNumber.")
                    }
                }
                "leave" -> {
                    if (!created) output("Parking lot is not created.")
                    else {
                        val leftOrNot = leave(input[1].toInt(), spots)
                        if (leftOrNot) output("Spot ${input[1]} is free.")
                        else output("There is no car in the spot ${input[1]}.")
                    }
                }
                "status" -> {
                    if (!created) output("Parking lot is not created.")
                    else {
                        val out = status(spots)
                        output(if (out.isEmpty()) "Parking lot is empty." else out.dropLast(1))
                    }
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
    for (i in spots.indices) {
        if (spots[i] == null) {
            spots[i] = Spot(unitNumber, unitColor)
            return i + 1 // spots start from 1
        }
    }
    return -1 // no empty spots
}

fun leave(spotNumber: Int, spots: Array<Spot?>): Boolean {
    if (spots[spotNumber - 1] == null) return false
    spots[spotNumber - 1] = null
    return true
}

fun status(spots: Array<Spot?>): String {
    val builder = StringBuilder()
    for ((i, spot) in spots.withIndex()) {
        if (spot != null) {
            builder.append(i + 1) // parking spot number (starts from 1)
            builder.append(' ')
            builder.append(spot)
            builder.append('\n')
        }
    }
    return builder.toString()
}
