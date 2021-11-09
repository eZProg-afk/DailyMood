package spiral.bit.dev.dailymood.ui.base.extensions

import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.graphics.drawable.toBitmap

fun Drawable.resizeTo(context: Context, size: Int) =
    BitmapDrawable(context.resources, toBitmap(size, size))
