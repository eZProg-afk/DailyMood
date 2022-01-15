package spiral.bit.dev.dailymood.ui.feature.registration.registrationTypes.models

import androidx.annotation.StringRes

data class ValidationResult(@StringRes val error: Int, val type: ValidationType)
enum class ValidationType { EMAIL, PASSWORD }