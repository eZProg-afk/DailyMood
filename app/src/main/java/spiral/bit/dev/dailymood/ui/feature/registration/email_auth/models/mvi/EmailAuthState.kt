package spiral.bit.dev.dailymood.ui.feature.registration.email_auth.models.mvi

import spiral.bit.dev.dailymood.ui.base.StateMarker
import spiral.bit.dev.dailymood.ui.feature.registration.models.RegScreenState

data class EmailAuthState(
    val regScreenState: RegScreenState
) : StateMarker