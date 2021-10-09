package spiral.bit.dev.dailymood.ui.common.utils

import android.view.animation.TranslateAnimation

object Animations {

    fun showAnimationInToX(): TranslateAnimation {
        return TranslateAnimation(-1000F, DEFAULT_X, DEFAULT_Y, DEFAULT_Y).apply {
            duration = 1000
        }
    }

    private const val DEFAULT_X = 0F
    private const val DEFAULT_Y = 0F
}