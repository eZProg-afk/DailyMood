package spiral.bit.dev.dailymood.ui.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.google.firebase.analytics.FirebaseAnalytics

infix fun FirebaseAnalytics.infixSendEvent(eventName: String) {
    Bundle().apply {
        putString(FirebaseAnalytics.Param.METHOD, eventName)
        logEvent(FirebaseAnalytics.Event.SIGN_UP, this)
    }
}

fun <STATE : StateMarker, SIDE_EFFECT : SideEffectMarker, BINDING : ViewBinding>
        BaseFragment<STATE, SIDE_EFFECT, BINDING>.binding(block: BINDING.() -> Unit) {
    binding?.apply(block)
}
