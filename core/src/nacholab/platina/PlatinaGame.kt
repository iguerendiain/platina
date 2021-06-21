package nacholab.platina

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import nacholab.platina.model.Country
import nacholab.platina.screens.MainGameplay
import kotlin.math.floor

class PlatinaGame: Game(){
	private val windowAspectRatio by lazy { Gdx.graphics.width.toFloat() / Gdx.graphics.height }

	val batch by lazy { SpriteBatch() }
	val cameraWidth = 240f
	val cameraHeight by lazy { cameraWidth / windowAspectRatio }
	val defaultFont: BitmapFont by lazy {
		val generator = FreeTypeFontGenerator(Gdx.files.internal("fonts/prstartk.ttf"))
		val font = generator.generateFont(FreeTypeFontGenerator.FreeTypeFontParameter().apply {
			size = 8
			mono = true
			kerning = false
		})
		generator.dispose()
		font
	}

	val country by lazy { Country() }

	private val mainGameplay by lazy { MainGameplay(this) }

	override fun create(){
		country.status.apply {
			assets = 0
			debt = 0
			subsidyPerPersonPerTick = 10f

			discontent = 0f
			publicServicesStatus = 1f
			population = 44000000
			employablePopulationFactor = .6f

			microEmployedPopulation = floor(population * employablePopulationFactor * .2f).toLong()
			mediumEmployedPopulation = floor(population * employablePopulationFactor * .3f).toLong()
			bigEmployedPopulation = floor(population * employablePopulationFactor * .4f).toLong()
			publiclyEmployedPopulation = floor(population * employablePopulationFactor * .05f).toLong()

			microAvailableJobs = 5000L
			mediumAvailableJobs = 10000L
			bigAvailableJobs = 20000L
			publicAvailableJobs = 1000L

			taxPerPersonPerTick = .3f
			taxPerMicroPerTick = .3f
			taxPerMediumPerTick = .3f
			taxPerBigPerTick = .3f

			publicAverageWagePerTick = 20f
			bigAverageWagePerTick = 25f
			mediumAverageWagePerTick = 30f
			microAverageWagePerTick = 35f

			publicServicesDecaySpeed = .001f

			debtAssetFactorDiscontentThreshold =  arrayOf(1f, 2f)
			debtAssetFactorBelowDiscontentFactor = -.0001f
			debtAssetFactorAboveDiscontentFactor = .001f
			nonDebtDiscontentDecaySpeed = -.001f
			gdppcDiscontentThreshold = arrayOf(50f, 300f)
			gdppcBelowDiscontentFactor = .01f
			gdppcAboveDiscontentFactor = -.000001f
			unemploymentDiscontentFactor = .001f
			personTaxDiscontentThreshold = arrayOf(.2f, .5f)
			personTaxBelowDiscontentFactor = -.0001f
			personTaxAboveDiscontentFactor = .001f
			microTaxDiscontentThreshold = arrayOf(.2f, .5f)
			microTaxBelowDiscontentFactor = -.0001f
			microTaxAboveDiscontentFactor = .001f
			mediumTaxDiscontentThreshold = arrayOf(.2f, .5f)
			mediumTaxBelowDiscontentFactor = -.0001f
			mediumTaxAboveDiscontentFactor = .001f
			bigTaxDiscontentThreshold = arrayOf(.2f, .5f)
			bigTaxBelowDiscontentFactor = -.0001f
			bigTaxAboveDiscontentFactor = .001f
			publicServicesDiscontentThreshold = arrayOf(.5f, .75f)
			publicServicesBelowDiscontentFactor = .1f
			publicServicesAboveDiscontentFactor = -.001f
			populationDecreaseSpeed = 2
			populationIncreaseSpeed = 3
			discontentMicroJobsThreshold = arrayOf(.5f, .75f)
			discontentBelowMicroJobsFactor = .01f
			discontentAboveMicroJobsFactor = -.01f
			discontentMediumJobsThreshold = arrayOf(.5f, .75f)
			discontentBelowMediumJobsFactor = .01f
			discontentAboveMediumJobsFactor = -.01f
			discontentBigJobsThreshold = arrayOf(.5f, .75f)
			discontentBelowBigJobsFactor = .01f
			discontentAboveBigJobsFactor = -.01f
			discontentPopulationThreshold = arrayOf(.5f, .75f)
			discontentBelowPopulationFactor = 1f
			discontentAbovePopulationFactor = -1f
		}

		setScreen(mainGameplay)
	}

	override fun render() {
		country.tick()
		super.render()
	}

	override fun dispose(){
	}
}