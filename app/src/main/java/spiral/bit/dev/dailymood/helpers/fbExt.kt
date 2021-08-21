package spiral.bit.dev.dailymood.helpers

import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

infix fun FirebaseAnalytics.infixSendEvent(eventName: String) {
    Bundle().apply {
        putString(FirebaseAnalytics.Param.METHOD, eventName)
        logEvent(FirebaseAnalytics.Event.SIGN_UP, this)
    }
}