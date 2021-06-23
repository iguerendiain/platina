package nacholab.platina.screens

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.ScreenUtils
import nacholab.platina.PlatinaGame

class MainGameplay(private val game: PlatinaGame): Screen {
    private val mainCamera by lazy { OrthographicCamera().apply {
        setToOrtho(false, game.cameraWidth, game.cameraHeight)
    }}

    override fun show() {

    }

    override fun render(delta: Float) {
        ScreenUtils.clear(.15f, .2f, .22f, 1f)
        game.batch.projectionMatrix = mainCamera.combined
        game.batch.begin()

        game.defaultFont.draw(
            game.batch,
            "TICKS (DAYS): ${game.country.tickCount}\n\n" + game.country.status.toString(),
            10f,
            game.cameraHeight-10
        )

        game.batch.end()
    }

    override fun resize(width: Int, height: Int) {

    }

    override fun pause() {

    }

    override fun resume() {

    }

    override fun hide() {

    }

    override fun dispose() {
        game.batch.dispose()
    }
}