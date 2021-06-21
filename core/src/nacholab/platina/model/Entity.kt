package nacholab.platina.model

abstract class Entity {
    abstract fun onCreate(status: CountryStatus)
    abstract fun tick(status: CountryStatus)
    abstract fun onDestroy(status: CountryStatus)
}