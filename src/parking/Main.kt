package parking

import java.util.*
import parking.Command.*

fun main() {
    Main().run()
}

class Main {
    private val scn = Scanner(System.`in`)
    private lateinit var spots: Array<Spot>

    fun run() {
        var on = true
        var created = false // parking lot

        var command: Command
        var input: List<String>

        while (on) {
            input = input()

            try {
                command = valueOf(input[0].toUpperCase())
            } catch (e: IllegalArgumentException) {
                output("Unknown command.")
                continue
            }

            if (!created && !(command == CREATE || command == EXIT)) {
                output("Parking lot is not created.")
                continue
            }

            output(when (command) {
                CREATE -> {
                    val size = input[1].toInt()
                    if (size < 1) "Incorrect input."
                    else {
                        var i = 0 // supplier
                        spots = Array(size) { Spot((++i).toString()) }
                        created = true
                        "Created a parking lot with $size spots."
                    }
                }
                PARK -> {
                    val spotIdentifier = park(Car(input[1], input[2]), spots)
                    if (spotIdentifier != null) "${input[2]} car parked on the spot $spotIdentifier."
                    else "Sorry, parking lot is full."
                }
                LEAVE -> {
                    val unitLeft = leave(input[1], spots)
                    if (unitLeft == true) "Spot ${input[1]} is free."
                    else if (unitLeft == false) "There is no car in the spot ${input[1]}."
                    else "Incorrect input"
                }
                STATUS -> {
                    val out = status(spots)
                    if (out.isEmpty()) "Parking lot is empty." else out.dropLast(1)
                }
                REG_BY_COLOR -> {
                    val out = regByColor(input[1], spots)
                    if (out.isNotEmpty()) out
                    else "No cars with color ${input[1]} were found."
                }
                SPOT_BY_COLOR -> {
                    val out = spotByColor(input[1], spots)
                    if (out.isNotEmpty()) out
                    else "No cars with color ${input[1]} were found."
                }
                SPOT_BY_REG -> {
                    val out = spotByReg(input[1], spots)
                    if (out.isNotEmpty()) out
                    else "No cars with registration number ${input[1]} where found."
                }
                EXIT -> {
                    on = false
                    ""
                }
            })
        }
    }

    private fun input(): List<String> = scn.nextLine().trim().split(Regex(" "))

    private fun output(str: String) = println(str)
}

fun park(car: Car, spots: Array<Spot>): String? {
    for (i in spots.indices) {
        if (spots[i].car == null) {
            spots[i].car = car
            return spots[i].spotIdentifier
        }
    }
    return null // no empty parking spots
}

fun leave(spotIdentifier: String, spots: Array<Spot>): Boolean? {
    for (spot in spots) {
        if (spot.spotIdentifier == spotIdentifier) {
            if (spot.car == null) return false
            else {
                spot.car = null
                return true
            }
        }
    }
    return null // incorrect identifier
}

fun status(spots: Array<Spot>): String {
    val builder = StringBuilder()
    for (spot in spots) {
        if (spot.car != null) {
            builder.append(spot)
            builder.append('\n')
        }
    }
    return builder.toString()
}

fun regByColor(color: String, spots: Array<Spot>): String {
    val builder = StringBuilder()
    spots.forEach { spot ->
        if (spot.car?.color.equals(color, true)) {
            builder.append(spot.car?.number)
            builder.append(", ")
        }
    }
    return builder.toString().removeSuffix(", ")
}

fun spotByColor(color: String, spots: Array<Spot>): String {
    val builder = StringBuilder()
    spots.forEach { spot ->
        if (spot.car?.color.equals(color, true)) {
            builder.append(spot.spotIdentifier)
            builder.append(", ")
        }
    }
    return builder.toString().removeSuffix(", ")
}

fun spotByReg(number: String, spots: Array<Spot>): String {
    spots.forEach { spot -> if (spot.car?.number == number) return spot.spotIdentifier }
    return ""
}
