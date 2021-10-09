package spiral.bit.dev.dailymood.ui.base

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import spiral.bit.dev.dailymood.R

fun ImageView.loadByUri(imageUri: String) =
    Glide.with(this)
        .load(Uri.parse(imageUri))
        .placeholder(R.drawable.ic_gallery)
        .into(this)

fun ImageView.loadByDrawable(drawable: Int) =
    Glide.with(this)
        .load(drawable)
        .placeholder(R.drawable.ic_gallery)
        .into(this)