package spiral.bit.dev.dailymood.ui.base

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import spiral.bit.dev.dailymood.R

fun ImageView.loadByUri(imageUri: Uri?) =
    Glide.with(this)
        .load(imageUri)
        .placeholder(R.drawable.ic_gallery)
        .into(this)

fun ImageView.loadByDrawable(drawable: Int) =
    Glide.with(this)
        .load(drawable)
        .placeholder(R.drawable.ic_gallery)
        .into(this)