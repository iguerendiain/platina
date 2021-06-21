package nacholab.platina.model

class Country {
    var tickTime = 1L
    var status = CountryStatus()
    var entities = arrayListOf<Entity>()

    private var lastTick = -1L

    fun tick(){
        val tickOffset = if (lastTick==-1L) tickTime else (System.currentTimeMillis() - lastTick)

        if (tickOffset >= tickTime){
            taxRevenue(status)
            publicServicesDecay(status)

            entities.forEach { entity -> entity.tick(status) }
            getEvent()?.execute(status)
            lastTick = System.currentTimeMillis()
        }
    }

    private fun publicServicesDecay(status: CountryStatus) {
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

    private fun taxRevenue(status: CountryStatus){
        val publicJobsMoney = status.publicAverageWagePerTick * status.publiclyEmployedPopulation
        val microJobsMoney = status.microAverageWagePerTick * status.microEmployedPopulation
        val mediumJobsMoney = status.mediumAverageWagePerTick * status.mediumEmployedPopulation
        val bigJobsMoney = status.bigAverageWagePerTick * status.bigEmployedPopulation
        val allMoney = microJobsMoney + mediumJobsMoney + bigJobsMoney + publicJobsMoney

        val personTaxMoney =  allMoney * status.taxPerPersonPerTick
        val microTaxMoney = microJobsMoney * status.taxPerMicroPerTick
        val mediumTaxMoney = mediumJobsMoney * status.taxPerMediumPerTick
        val bigTaxMoney = bigJobsMoney * status.taxPerBigPerTick

        val totalRevenue = personTaxMoney + microTaxMoney + mediumTaxMoney + bigTaxMoney

        status.assets+=totalRevenue.toLong()
    }

}