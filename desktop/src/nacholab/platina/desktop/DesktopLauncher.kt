package nacholab.platina.desktop

import com.badlogic.gdx.backends.lwjgl.LwjglApplication
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration
import nacholab.platina.PlatinaGame

class DesktopLauncher {

    companion object{
        @JvmStatic
        fun main (arg: Array<String>){
            val config = LwjglApplicationConfiguration().apply {
                width = 360
                height = 640
                resizable = false
                undecorated = true
                foregroundFPS = 0
                backgroundFPS = 24
            }

            LwjglApplication(PlatinaGame(), config)
        }
    }
}