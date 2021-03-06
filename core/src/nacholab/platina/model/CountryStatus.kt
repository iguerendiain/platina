package nacholab.platina.model

import nacholab.platina.NachoLabMathUtils
import java.lang.StringBuilder
import kotlin.math.floor

class CountryStatus {
    var assets = 0L
    var debt = 0L

    var population = 0L

    var employablePopulationFactor = 0f
    var microEmployedPopulation = 0L
    var mediumEmployedPopulation = 0L
    var bigEmployedPopulation = 0L
    var publiclyEmployedPopulation = 0L

    var discontent = 0f
    var publicServicesStatus = 0f

    var microAvailableJobs = 0L
    var mediumAvailableJobs = 0L
    var bigAvailableJobs = 0L
    var publicAvailableJobs = 0L

    var taxPerPersonPerTick = 0f
    var taxPerMicroPerTick = 0f
    var taxPerMediumPerTick = 0f
    var taxPerBigPerTick = 0f

    var microAverageWagePerTick = 0f
    var mediumAverageWagePerTick = 0f
    var bigAverageWagePerTick = 0f
    var publicAverageWagePerTick = 0f

    var subsidyPerPersonPerTick = 0f

    var publicServicesDecaySpeed = 0f

    var discontentIncreaseSpeed = 0f
    var discontentPublicServicesMaxFactor = 0f
    var discontentPersonTaxThreshold = 0f
    var discontentPersonTaxMaxFactor = 0f
    var discontentMicroTaxThreshold = 0f
    var discontentMicroTaxMaxFactor = 0f
    var discontentMediumTaxThreshold = 0f
    var discontentMediumTaxMaxFactor = 0f
    var discontentBigTaxThreshold = 0f
    var discontentBigTaxMaxFactor = 0f

    fun calculateUnemploymentFactor(): Float {
        val totalEmployment = microEmployedPopulation + mediumEmployedPopulation + bigEmployedPopulation + publiclyEmployedPopulation
        return 1 - totalEmployment / (population * employablePopulationFactor)
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()

        stringBuilder.append("MONEY\n")
        stringBuilder.append("Assets: $${assets}\n")
        stringBuilder.append("Debt: $${debt}\n")
        stringBuilder.append("Subsidies: $$subsidyPerPersonPerTick\n\n")

        stringBuilder.append("THE BIG PICTURE:\n")
        stringBuilder.append("Discontent: $discontent\n")
        stringBuilder.append("Utilities: $publicServicesStatus\n")
        stringBuilder.append("Population: $population\n")
        stringBuilder.append("Employable: ${population * employablePopulationFactor}\n")
        stringBuilder.append("Unemployment: ${floor(calculateUnemploymentFactor() * 100).toInt()}%\n\n")

        stringBuilder.append("EMPLOYMENT:\n")
        stringBuilder.append("Micro: $microEmployedPopulation\n")
        stringBuilder.append("Medium: $mediumEmployedPopulation\n")
        stringBuilder.append("Big: $bigEmployedPopulation\n")
        stringBuilder.append("Public: $publiclyEmployedPopulation\n\n")

        stringBuilder.append("AVAILABLE JOBS:\n")
        stringBuilder.append("Micro: $microAvailableJobs\n")
        stringBuilder.append("Medium: $mediumAvailableJobs\n")
        stringBuilder.append("Big: $bigAvailableJobs\n")
        stringBuilder.append("Public: $publicAvailableJobs\n\n")

        stringBuilder.append("TAXES:\n")
        stringBuilder.append("Persons: $taxPerPersonPerTick\n")
        stringBuilder.append("Micro: $taxPerMicroPerTick\n")
        stringBuilder.append("Medium: $taxPerMediumPerTick\n")
        stringBuilder.append("Big: $taxPerBigPerTick\n\n")

        stringBuilder.append("AVERAGE WAGE:\n")
        stringBuilder.append("Micro: $$microAverageWagePerTick\n")
        stringBuilder.append("Medium: $$mediumAverageWagePerTick\n")
        stringBuilder.append("Big: $$bigAverageWagePerTick\n")
        stringBuilder.append("Public: $$publicAverageWagePerTick")

        return stringBuilder.toString()
    }

    fun discontentFactorBasedOnServicesDecay() =
        NachoLabMathUtils.inverseFactor(publicServicesStatus, discontentPublicServicesMaxFactor)

    fun discontentFactorBasedOnTaxation() =
        discontentFactorBasedOnPersonsTaxation() +
        discontentFactorBasedOnMicroTaxation() +
        discontentFactorBasedOnMediumTaxation() +
        discontentFactorBasedOnBigTaxation()

    private fun discontentFactorBasedOnPersonsTaxation() = NachoLabMathUtils.inverseFactorWithMinimum(
        taxPerPersonPerTick,
        discontentPersonTaxThreshold,
        discontentPersonTaxMaxFactor
    )

    private fun discontentFactorBasedOnMicroTaxation() = NachoLabMathUtils.inverseFactorWithMinimum(
        taxPerMicroPerTick,
        discontentMicroTaxMaxFactor,
        discontentMicroTaxMaxFactor
    )

    private fun discontentFactorBasedOnMediumTaxation() = NachoLabMathUtils.inverseFactorWithMinimum(
        taxPerMediumPerTick,
        discontentMediumTaxThreshold,
        discontentMediumTaxMaxFactor
    )

    private fun discontentFactorBasedOnBigTaxation() = NachoLabMathUtils.inverseFactorWithMinimum(
        taxPerBigPerTick,
        discontentBigTaxThreshold,
        discontentBigTaxMaxFactor
    )

    fun calculateTaxRevenue(): Long{
        val publicJobsMoney = publicAverageWagePerTick * publiclyEmployedPopulation
        val microJobsMoney = microAverageWagePerTick * microEmployedPopulation
        val mediumJobsMoney = mediumAverageWagePerTick * mediumEmployedPopulation
        val bigJobsMoney = bigAverageWagePerTick * bigEmployedPopulation
        val allMoney = microJobsMoney + mediumJobsMoney + bigJobsMoney + publicJobsMoney

        val personTaxMoney =  allMoney * taxPerPersonPerTick
        val microTaxMoney = microJobsMoney * taxPerMicroPerTick
        val mediumTaxMoney = mediumJobsMoney * taxPerMediumPerTick
        val bigTaxMoney = bigJobsMoney * taxPerBigPerTick

        val totalRevenue = personTaxMoney + microTaxMoney + mediumTaxMoney + bigTaxMoney

        return totalRevenue.toLong()
    }
}