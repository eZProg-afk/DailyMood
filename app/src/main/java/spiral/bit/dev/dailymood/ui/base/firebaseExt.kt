package spiral.bit.dev.dailymood.ui.base

import android.os.Bundle
import androidx.viewbinding.ViewBinding
import com.google.firebase.analytics.FirebaseAnalytics

//TODO
infix fun FirebaseAnalytics.infixSendEvent(eventName: String) {
    Bundle().apply {
        putString(FirebaseAnalytics.Param.METHOD, eventName)
        logEvent(FirebaseAnalytics.Event.SIGN_UP, this)
    }
}