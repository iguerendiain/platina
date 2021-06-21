package nacholab.platina.model

import kotlin.math.ceil
import kotlin.math.floor

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
            discontentChange(status)
            populationChange(status)
            availableJobsChangePerDiscontent(status)

            entities.forEach { entity -> entity.tick(status) }
            getEvent()?.execute(status)
            lastTick = System.currentTimeMillis()
        }
    }

    private fun publicServicesDecay(status: CountryStatus) {
        status.publicServicesStatus -= status.publicServicesDecaySpeed
        status.publicServicesStatus = status.publicServicesStatus.coerceAtLeast(0f)
    }

    private fun thresholdFactorCalculator(value: Float, threshold: Array<Float>, belowFactor: Float, aboveFactor: Float): Float{
        return when {
            value < threshold[0] -> {
                val offset = threshold[0] - value
                offset * belowFactor
            }
            value > threshold[1] -> {
                val offset = value - threshold[1]
                offset * aboveFactor
            }
            else -> {
                0f
            }
        }
    }

    private fun debtAssetFactorDiscontentChange(status: CountryStatus){
        status.discontent += thresholdFactorCalculator(
            status.debt / status.assets.toFloat(),
            status.debtAssetFactorDiscontentThreshold,
            status.debtAssetFactorBelowDiscontentFactor,
            status.debtAssetFactorAboveDiscontentFactor
        )
    }

    private fun nonDebtDiscontentChange(status: CountryStatus){
        if (status.debt == 0L) status.discontent -= status.nonDebtDiscontentDecaySpeed
    }

    private fun gdpccDiscontentChange(status: CountryStatus){
        status.discontent += thresholdFactorCalculator(
            status.assets / status.population.toFloat(),
            status.gdppcDiscontentThreshold,
            status.gdppcBelowDiscontentFactor,
            status.gdppcAboveDiscontentFactor
        )
    }

    private fun unemploymentDiscontentChange(status: CountryStatus){
        status.discontent += status.calculateUnemploymentFactor() * status.unemploymentDiscontentFactor
    }

    private fun taxationDiscontentChange(status: CountryStatus){
        personTaxationDiscontentChange(status)
        microTaxationDiscontentChange(status)
        mediumTaxationDiscontentChange(status)
        bigTaxationDiscontentChange(status)
    }

    private fun personTaxationDiscontentChange(status: CountryStatus){
        status.discontent += thresholdFactorCalculator(
            status.taxPerPersonPerTick,
            status.personTaxDiscontentThreshold,
            status.personTaxBelowDiscontentFactor,
            status.personTaxAboveDiscontentFactor
        )
    }

    private fun microTaxationDiscontentChange(status: CountryStatus){
        status.discontent += thresholdFactorCalculator(
            status.taxPerMicroPerTick,
            status.microTaxDiscontentThreshold,
            status.microTaxBelowDiscontentFactor,
            status.microTaxAboveDiscontentFactor
        )
    }

    private fun mediumTaxationDiscontentChange(status: CountryStatus){
        status.discontent += thresholdFactorCalculator(
            status.taxPerMediumPerTick,
            status.mediumTaxDiscontentThreshold,
            status.mediumTaxBelowDiscontentFactor,
            status.mediumTaxAboveDiscontentFactor
        )
    }

    private fun bigTaxationDiscontentChange(status: CountryStatus){
        status.discontent += thresholdFactorCalculator(
            status.taxPerBigPerTick,
            status.bigTaxDiscontentThreshold,
            status.bigTaxBelowDiscontentFactor,
            status.bigTaxAboveDiscontentFactor
        )
    }

    private fun publicServicesDiscontentChange(status: CountryStatus){
        status.discontent += thresholdFactorCalculator(
            status.publicServicesStatus,
            status.publicServicesDiscontentThreshold,
            status.publicServicesBelowDiscontentFactor,
            status.publicServicesAboveDiscontentFactor
        )
    }

    private fun discontentChange(status: CountryStatus) {
        debtAssetFactorDiscontentChange(status)
        nonDebtDiscontentChange(status)
        gdpccDiscontentChange(status)
        unemploymentDiscontentChange(status)
        taxationDiscontentChange(status)
        publicServicesDiscontentChange(status)
        status.discontent = status.discontent.coerceAtLeast(0f).coerceAtMost(1f)
    }

    private fun microJobsChangeFactor(status: CountryStatus) = thresholdFactorCalculator(
        status.discontent,
        status.discontentMicroJobsThreshold,
        status.discontentBelowMicroJobsFactor,
        status.discontentAboveMicroJobsFactor
    )

    private fun mediumJobsChangeFactor(status: CountryStatus) = thresholdFactorCalculator(
        status.discontent,
        status.discontentMediumJobsThreshold,
        status.discontentBelowMediumJobsFactor,
        status.discontentAboveMediumJobsFactor
    )

    private fun bigJobsChangeFactor(status: CountryStatus) = thresholdFactorCalculator(
        status.discontent,
        status.discontentBigJobsThreshold,
        status.discontentBelowBigJobsFactor,
        status.discontentAboveBigJobsFactor
    )

    private fun availableJobsChangePerDiscontent(status: CountryStatus) {
        when{
            status.microAvailableJobs > 0 ->
                status.microAvailableJobs += floor(microJobsChangeFactor(status) * status.microAvailableJobs).toLong()
            status.microAvailableJobs < 0 ->
                status.microEmployedPopulation += status.microAvailableJobs
            status.microAvailableJobs == 0L ->
                status.microEmployedPopulation += floor(microJobsChangeFactor(status) * status.microEmployedPopulation).toLong()
        }

        when{
            status.mediumAvailableJobs > 0 ->
                status.mediumAvailableJobs += floor(mediumJobsChangeFactor(status) * status.mediumAvailableJobs).toLong()
            status.mediumAvailableJobs < 0 ->
                status.mediumEmployedPopulation += status.mediumAvailableJobs
            status.mediumAvailableJobs == 0L ->
                status.mediumEmployedPopulation += floor(mediumJobsChangeFactor(status) * status.mediumEmployedPopulation).toLong()
        }

        when{
            status.bigAvailableJobs > 0 ->
                status.bigAvailableJobs += floor(bigJobsChangeFactor(status) * status.bigAvailableJobs).toLong()
            status.bigAvailableJobs < 0 ->
                status.bigEmployedPopulation += status.bigAvailableJobs
            status.bigAvailableJobs == 0L ->
                status.bigEmployedPopulation += floor(bigJobsChangeFactor(status) * status.bigEmployedPopulation).toLong()
        }
    }

    private fun populationChange(status: CountryStatus) {
        val populationFactorByDiscontent = thresholdFactorCalculator(
            status.discontent,
            status.discontentPopulationThreshold,
            status.discontentBelowPopulationFactor,
            status.discontentAbovePopulationFactor
        )

        val increaseFactor = 1 + if (populationFactorByDiscontent > 0) populationFactorByDiscontent else 0f
        val decreaseFactor = 1 + if (populationFactorByDiscontent < 0) populationFactorByDiscontent else 0f

        status.population -= ceil(status.populationDecreaseSpeed * decreaseFactor).toLong()
        status.population += ceil(status.populationIncreaseSpeed * increaseFactor).toLong()
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