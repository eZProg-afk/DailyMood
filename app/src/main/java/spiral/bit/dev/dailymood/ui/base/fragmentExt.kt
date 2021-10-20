package spiral.bit.dev.dailymood.ui.base

import androidx.fragment.app.Fragment

fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}