package parking

class Spot(val unitNumber: String, val unitColor: String) {
    override fun toString(): String {
        return "$unitNumber $unitColor"
    }
}
