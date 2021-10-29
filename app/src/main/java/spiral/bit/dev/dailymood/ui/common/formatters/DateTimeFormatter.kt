package spiral.bit.dev.dailymood.ui.common.formatters

import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class DateTimeFormatter {

    private val FILE_NAME_FORMAT = "yy-MM-dd"
    private val FILE_EXTENSION = ".jpg"

    fun format(createdTime: Long): String? {
        return SimpleDateFormat.getInstance().format(createdTime)
    }

    fun formatFile(file: File): File {
        return File(
            file,
            SimpleDateFormat(
                FILE_NAME_FORMAT,
                Locale.getDefault()
            ).format(System.currentTimeMillis()) + FILE_EXTENSION
        )
    }
}