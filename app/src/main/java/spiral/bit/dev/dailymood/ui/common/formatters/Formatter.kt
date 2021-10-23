package spiral.bit.dev.dailymood.ui.common.formatters

import java.text.SimpleDateFormat

class Formatter {

    fun format(createdTime: Long): String? {
        return SimpleDateFormat.getInstance().format(createdTime)
    }
}