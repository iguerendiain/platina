package nacholab.platina.model

abstract class Event {
    abstract fun execute(status: CountryStatus)
}