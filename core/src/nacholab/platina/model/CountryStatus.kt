package nacholab.platina.model

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
    var publicAverageWagePerTick = 0f;

    var subsidyPerPersonPerTick = 0f

    var publicServicesDecaySpeed = 0f

    // Threshold to determine how many discontent change happens based on debt/asset factor
    var debtAssetFactorDiscontentThreshold = arrayOf(0f, 0f)

    // Factor to multiply the offset outside of the threshold to either increase or decrease discontent based on debt/asset factor
    var debtAssetFactorBelowDiscontentFactor = 0f
    var debtAssetFactorAboveDiscontentFactor = 0f

    // Amount of discontent decay per tick when debt is zero
    var nonDebtDiscontentDecaySpeed = 0f

    // Threshold to determine how many discontent change happens based on GDPPC (Gross Domestic Product Per Capita)
    var gdppcDiscontentThreshold = arrayOf(0f, 0f)

    // Factor to multiply the offset outside of the threshold to either increase or decrease discontent based on GDPPC
    var gdppcBelowDiscontentFactor = 0f
    var gdppcAboveDiscontentFactor = 0f

    // Unemployment discontent factor
    var unemploymentDiscontentFactor = 0f

    // Threshold to determine how many discontent change happens based on taxation (Persons)
    var personTaxDiscontentThreshold = arrayOf(0f, 0f)

    // Factor to multiply the offset outside of the threshold to either increase or decrease discontent based on taxation (Persons)
    var personTaxBelowDiscontentFactor = 0f
    var personTaxAboveDiscontentFactor = 0f

    // Threshold to determine how many discontent change happens based on taxation (Small companies)
    var microTaxDiscontentThreshold = arrayOf(0f, 0f)

    // Factor to multiply the offset outside of the threshold to either increase or decrease discontent based on taxation (Small companies)
    var microTaxBelowDiscontentFactor = 0f
    var microTaxAboveDiscontentFactor = 0f

    // Threshold to determine how many discontent change happens based on taxation (Medium companies)
    var mediumTaxDiscontentThreshold = arrayOf(0f, 0f)

    // Factor to multiply the offset outside of the threshold to either increase or decrease discontent based on taxation (Medium companies)
    var mediumTaxBelowDiscontentFactor = 0f
    var mediumTaxAboveDiscontentFactor = 0f

    // Threshold to determine how many discontent change happens based on taxation (Big companies)
    var bigTaxDiscontentThreshold = arrayOf(0f, 0f)

    // Factor to multiply the offset outside of the threshold to either increase or decrease discontent based on taxation (Big companies)
    var bigTaxBelowDiscontentFactor = 0f
    var bigTaxAboveDiscontentFactor = 0f

    // Threshold to determine how many discontent change happens based on public services
    var publicServicesDiscontentThreshold = arrayOf(0f, 0f)

    // Factor to multiply the offset outside of the threshold to either increase or decrease discontent based on public services
    var publicServicesBelowDiscontentFactor = 0f
    var publicServicesAboveDiscontentFactor = 0f

    // Deaths or emigrations per tick
    var populationDecreaseSpeed = 0

    // Births or immigrations per tick
    var populationIncreaseSpeed = 0

    // Threshold to determine how many micro available jobs change happens based on discontent
    var discontentMicroJobsThreshold = arrayOf(0f, 0f)

    // Factor to multiply the offset outside of the threshold to either increase or decrease micro available jobs based on discontent
    var discontentBelowMicroJobsFactor = 0f
    var discontentAboveMicroJobsFactor = 0f

    // Threshold to determine how many medium available jobs change happens based on discontent
    var discontentMediumJobsThreshold = arrayOf(0f, 0f)

    // Factor to multiply the offset outside of the threshold to either increase or decrease medium available jobs based on discontent
    var discontentBelowMediumJobsFactor = 0f
    var discontentAboveMediumJobsFactor = 0f

    // Threshold to determine how many big available jobs change happens based on discontent
    var discontentBigJobsThreshold = arrayOf(0f, 0f)

    // Factor to multiply the offset outside of the threshold to either increase or decrease big available jobs based on discontent
    var discontentBelowBigJobsFactor = 0f
    var discontentAboveBigJobsFactor = 0f

    // Threshold to determine how fast the population change based on discontent
    var discontentPopulationThreshold = arrayOf(0f, 0f)

    // Factor to multiply the offset outside of the threshold to either increase or decrease population based on discontent
    var discontentBelowPopulationFactor = 0f
    var discontentAbovePopulationFactor = 0f

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
}