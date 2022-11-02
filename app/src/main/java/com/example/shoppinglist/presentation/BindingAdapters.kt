package com.example.shoppinglist.presentation

import androidx.databinding.BindingAdapter
import com.example.shoppinglist.R
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("errorInputName")
fun bindErrorInputName(textInputLayout: TextInputLayout, isError: Boolean) {
    if (isError)
        textInputLayout.error = textInputLayout.context.getString(R.string.error_input_name)
    else
        textInputLayout.error = null
}

@BindingAdapter("errorInputCount")
fun bindErrorInputCount(textInputLayout: TextInputLayout, isError: Boolean) {
    if (isError)
        textInputLayout.error = textInputLayout.context.getString(R.string.error_input_count)
    else
        textInputLayout.error = null
}