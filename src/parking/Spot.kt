package parking

class Spot(val spotIdentifier: String, var car: Car? = null) {

    override fun toString(): String = "$spotIdentifier $car"
}
