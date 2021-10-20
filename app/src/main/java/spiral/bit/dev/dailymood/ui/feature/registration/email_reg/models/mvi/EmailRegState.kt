package spiral.bit.dev.dailymood.ui.feature.registration.email_reg.models.mvi

import com.facebook.AccessToken
import spiral.bit.dev.dailymood.ui.base.StateMarker

data class EmailRegState(
    val facebookToken: AccessToken
) : StateMarker