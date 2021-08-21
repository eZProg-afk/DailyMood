package spiral.bit.dev.dailymood.helpers

import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.Glide
import spiral.bit.dev.dailymood.R

infix fun ImageView.infixLoad(imageUri: String) =
    Glide.with(this)
        .load(Uri.parse(imageUri))
        .placeholder(R.drawable.ic_gallery)
        .into(this)

infix fun ImageView.loadDrawable(drawable: Int) =
    Glide.with(this)
        .load(drawable)
        .placeholder(R.drawable.ic_gallery)
        .into(this)