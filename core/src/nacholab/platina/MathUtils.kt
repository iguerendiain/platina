package nacholab.platina

class NachoLabMathUtils {
    companion object{
        fun inverseFactor(basedOn: Float, maxFactor: Float) = maxFactor - basedOn * maxFactor

        fun inverseFactorWithMinimum(basedOn: Float, minimum: Float, maxFactor: Float) =
            if (basedOn < minimum) 0f
            else inverseFactor(basedOn - minimum, maxFactor)
    }
}