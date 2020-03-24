package parking

import java.util.*
import parking.Command.*

fun main() {
    ParkingLot().run()
}

class ParkingLot {
    private val scn = Scanner(System.`in`)
    private var spots: Array<Spot> = arrayOf()
    private var on = true
    private var created = false // parking lot

    fun run() {
        var input: List<String>
        var command: Command

        while (on) {
            input = input()

            try {
                command = valueOf(input[0].toUpperCase())
            } catch (e: IllegalArgumentException) {
                output("Unknown command.")
                continue
            }

            val errMessage = inputCheck(input, command, spots)
            if (errMessage.isNotEmpty()) { // incorrect input
                output(errMessage)
                continue
            }

            output(executeCommand(command, input))
        }
    }

    private fun executeCommand(command: Command, input: List<String>): String {
        return when (command) {
            CREATE -> {
                val size = input[1].toInt()
                var i = 0 // supplier
                spots = Array(size) { Spot((++i).toString()) }
                created = true
                "Created a parking lot with $size spots."
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
                else "Error, incorrect parking spot identifier."
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
                else "No cars with registration number ${input[1]} were found."
            }
            EXIT -> {
                on = false
                ""
            }
        }
    }

    private fun inputCheck(input: List<String>, command: Command, spots: Array<Spot>): String {
        val inputSize = input.size

        // check input size
        val correctSize = when (command) {
            EXIT, STATUS -> inputSize == 1
            CREATE, LEAVE, REG_BY_COLOR, SPOT_BY_COLOR, SPOT_BY_REG -> inputSize == 2
            PARK -> inputSize == 3
        }
        if (!correctSize) return "Error, incorrect number of arguments."

        //
        if (!created && !(command == CREATE || command == EXIT)) {
            return "Parking lot is not created."
        }

        // check casts
        val correctCasts = when (command) {
            CREATE -> input[1].toIntOrNull() != null
            else -> true
        }
        if (!correctCasts) return "Error, incorrect input."

        // check special cases
        val errSpecialCases: String = when (command) {
            CREATE -> if (input[1].toInt() < 1) "Error, incorrect value." else emptyStr
            // check car number
            PARK -> {
                var out = emptyStr
                for (spot in spots) {
                    if (spot.car?.number == input[1]) {
                        out = "Error, the car with the number ${input[1]} already parked."
                        break
                    }
                }
                out
            }
            else -> emptyStr
        }
        if (errSpecialCases.isNotEmpty()) return errSpecialCases

        return emptyStr // correct input
    }

    private fun input(): List<String> = scn.nextLine().trim().split(Regex(" "))

    private fun output(str: String) = println(str)
}

private const val emptyStr = ""

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
