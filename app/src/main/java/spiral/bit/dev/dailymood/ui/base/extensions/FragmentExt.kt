package spiral.bit.dev.dailymood.ui.base

import androidx.fragment.app.Fragment
import spiral.bit.dev.dailymood.ui.base.extensions.hideKeyboard

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}