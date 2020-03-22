package parking

import java.util.*

fun main() {
    Main().run()
}

class Main {
    private val scn = Scanner(System.`in`)
    private lateinit var spots: Array<Spot>

    fun run() {
        var on = true
        var created = false // parking lot

        while (on) {
            val input = input()

            output(when (input[0].toLowerCase()) {
                "create" -> {
                    val size = input[1].toInt()
                    if (size < 1) "Incorrect input."
                    else {
                        var i = 0 // supplier
                        spots = Array(size) { Spot((++i).toString()) }
                        created = true
                        "Created a parking lot with $size spots."
                    }
                }
                "park" -> {
                    if (!created) "Parking lot is not created."
                    else {
                        val spotIdentifier = park(Car(input[1], input[2]), spots)
                        if (spotIdentifier != null) "${input[2]} car parked on the spot $spotIdentifier."
                        else "Sorry, parking lot is full."
                    }
                }
                "leave" -> {
                    if (!created) "Parking lot is not created."
                    else {
                        val unitLeft = leave(input[1], spots)
                        if (unitLeft == true) "Spot ${input[1]} is free."
                        else if (unitLeft == false) "There is no car in the spot ${input[1]}."
                        else "Incorrect input"
                    }
                }
                "status" -> {
                    if (!created) "Parking lot is not created."
                    else {
                        val out = status(spots)
                        if (out.isEmpty()) "Parking lot is empty." else out.dropLast(1)
                    }
                }
                "exit" -> {
                    on = false
                    ""
                }
                else -> "Incorrect input."
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
