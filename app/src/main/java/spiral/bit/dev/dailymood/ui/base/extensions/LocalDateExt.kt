package spiral.bit.dev.dailymood.ui.base.extensions

import java.time.LocalDate
import java.time.ZoneId

fun LocalDate.getStartOfDay(): Long {
    return atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
}