package spiral.bit.dev.dailymood.ui.base

import android.app.Activity
import android.view.View
import spiral.bit.dev.dailymood.ui.base.extensions.hideKeyboard

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}