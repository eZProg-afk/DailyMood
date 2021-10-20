package spiral.bit.dev.dailymood.ui.base

import android.app.Activity
import android.view.View

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}