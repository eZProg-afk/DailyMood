package spiral.bit.dev.dailymood.ui.base

import android.view.View
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar

fun Snackbar.action(@StringRes actionRes: Int, listener: (View) -> Unit) {
    action(view.resources.getString(actionRes), listener)
}

fun Snackbar.action(action: String, listener: (View) -> Unit) {
    setAction(action, listener)
}