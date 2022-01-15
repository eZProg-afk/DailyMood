package spiral.bit.dev.dailymood.ui.base.extensions

import android.widget.Spinner
import androidx.annotation.ArrayRes
import androidx.annotation.LayoutRes

//Our api for creating spinner with stub data in default parameters

fun Spinner.configureSpinner(
    @ArrayRes arrayEntries: Int = android.R.array.phoneTypes,
    @LayoutRes customTextViewResource: Int = android.R.layout.simple_spinner_item,
    @LayoutRes dropDownViewResource: Int = android.R.layout.simple_spinner_dropdown_item,
    itemSelect: (Int) -> Unit,
    itemNothingSelect: (() -> Unit)? = null
) {
    rootView.context.apply {
        adapter = createSpinnerAdapter(
            arrayResourceId = arrayEntries,
            customTextViewResource = customTextViewResource,
            dropDownViewResource = dropDownViewResource
        )
        onItemSelectedListener = setSpinnerItemSelectListener(itemSelect, itemNothingSelect)
    }
}