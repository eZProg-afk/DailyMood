package spiral.bit.dev.dailymood.ui.base

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View.OnTouchListener
import android.widget.EditText
import androidx.annotation.DrawableRes
import androidx.core.widget.doOnTextChanged
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import spiral.bit.dev.dailymood.R

@ExperimentalCoroutinesApi
fun EditText.textChanges(editTextConfig: EditTextConfig? = null): Flow<CharSequence?> {
    return callbackFlow {
        val listener = doOnTextChanged { text, _, _, _ ->
            editTextConfig?.let { config ->
                setOnTouchCompoundButtonListener(config)
                setCompoundButtonIcon(config)
            }
            trySend(text)
        }
        awaitClose { removeTextChangedListener(listener) }
    }
}

private fun EditText.setCompoundButtonIcon(editTextConfig: EditTextConfig) {
    if (editTextConfig.drawableGravity.constantValue == DrawableGravity.START.constantValue) {
        setCompoundDrawablesRelativeWithIntrinsicBounds(
            editTextConfig.drawableIcon,
            NONE_DRAWABLE,
            NONE_DRAWABLE,
            NONE_DRAWABLE
        )
    } else if (editTextConfig.drawableGravity.constantValue == DrawableGravity.END.constantValue){
        setCompoundDrawablesRelativeWithIntrinsicBounds(
            NONE_DRAWABLE,
            NONE_DRAWABLE,
            editTextConfig.drawableIcon,
            NONE_DRAWABLE
        )
    }
}

@SuppressLint("ClickableViewAccessibility")
private fun EditText.setOnTouchCompoundButtonListener(editTextConfig: EditTextConfig) {
    setOnTouchListener(OnTouchListener { _, event ->
        if (event.action == MotionEvent.ACTION_UP) {
            onClearIconClick(editTextConfig)
            return@OnTouchListener true
        }
        false
    })
}

private fun EditText.onClearIconClick(editTextConfig: EditTextConfig) {
    setText("")
    clearFocus()
    context.hideKeyboard(this)
    setCompoundButtonIcon(EditTextConfig(editTextConfig.drawableGravity, R.drawable.ic_search))
}

enum class DrawableGravity(val constantValue: Int) { START(0), END(2) }
class EditTextConfig(val drawableGravity: DrawableGravity, @DrawableRes val drawableIcon: Int)

private const val NONE_DRAWABLE = 0
