package spiral.bit.dev.dailymood.ui.base.extensions

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.ArrayRes
import androidx.annotation.LayoutRes
import androidx.core.app.ActivityCompat

fun Context.hasPermissions(permission: String) = ActivityCompat.checkSelfPermission(
    this, permission
) == PackageManager.PERMISSION_GRANTED

fun Context.hideKeyboard(view: View) {
    val inputManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(view.windowToken, 0)
}

// Create and set adapter on spinner

fun Context.createSpinnerAdapter(
    @ArrayRes arrayResourceId: Int,
    @LayoutRes customTextViewResource: Int,
    @LayoutRes dropDownViewResource: Int
) = ArrayAdapter.createFromResource(
    this,
    arrayResourceId,
    customTextViewResource
).apply {
    setDropDownViewResource(dropDownViewResource)
}


// Create and set on spinner itemSelectedListener with optional second listener

fun setSpinnerItemSelectListener(
    itemSelect: (Int) -> Unit,
    itemNothingSelect: (() -> Unit)? = null
) = object : AdapterView.OnItemSelectedListener {
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        itemSelect.invoke(position)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        itemNothingSelect?.invoke()
    }
}