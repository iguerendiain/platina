package nacholab.platina.model

class Country {
    var tickTime = 10L
    var status = CountryStatus()
    var entities = arrayListOf<Entity>()
    var tickCount = 0L

    private var lastTick = -1L

    fun tick(){
        val tickOffset = if (lastTick==-1L) tickTime else (System.currentTimeMillis() - lastTick)

        if (tickOffset >= tickTime){
            status.assets += status.calculateTaxRevenue()
            decayPublicServices(status)
            val discontentFactor =
                status.discontentFactorBasedOnServicesDecay() +
                status.discontentFactorBasedOnTaxation()

            updateDiscontent(status, discontentFactor)

            entities.forEach { entity -> entity.tick(status) }
            getEvent()?.execute(status)
            lastTick = System.currentTimeMillis()
            tickCount++
        }
    }

    private fun updateDiscontent(status: CountryStatus, discontentFactor: Float){
        status.discontent += status.discontentIncreaseSpeed * discontentFactor
        status.discontent = status
            .discontent
            .coerceAtMost(1f)
            .coerceAtLeast(0f)

    }

    private fun decayPublicServices(status: CountryStatus) {
        status.publicServicesStatus -= status.publicServicesDecaySpeed
        status.publicServicesStatus = status.publicServicesStatus.coerceAtLeast(0f)
    }

    fun addEntity(entity: Entity){
        entity.onCreate(status)
        entities.add(entity)
    }

    fun removeEntity(entity: Entity){
        entities.remove(entity)
        entity.onDestroy(status)
    }

    private fun getEvent(): Event?{
        return null
    }

}