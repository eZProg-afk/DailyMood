package spiral.bit.dev.dailymood.ui.feature.analytics.models

import androidx.annotation.StringRes
import spiral.bit.dev.dailymood.R

enum class AnalyticsSortOrder(var period: Int, @StringRes val periodName: Int) {
    LAST_TEN_DAYS(10, R.string.ten_days_label),
    LAST_MONTH(30, R.string.last_month_label),
    LAST_SIX_MONTHS(180, R.string.last_six_months_label),
    LAST_YEAR(365, R.string.year_label)
}