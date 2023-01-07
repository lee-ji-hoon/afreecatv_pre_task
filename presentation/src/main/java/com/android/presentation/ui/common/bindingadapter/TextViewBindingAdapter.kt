package com.android.presentation.ui.common.bindingadapter

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.DecimalFormat

private val decimalFormat = DecimalFormat("#,###")

@BindingAdapter("priceAmount")
fun applyPriceFormat(view: TextView, price: Int) {
    view.text = decimalFormat.format(price)
}
