package com.rentalhomes.utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

class MoneyTextFormatter(var editText: EditText,var pattern: String): TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        editText.removeTextChangedListener(this)

        try {
            var originalString = s.toString()
            if (originalString.contains(",")) {
                originalString = originalString.replace(",".toRegex(), "")
            }
            val longVal: Long = originalString.toLong()
            val formatter: DecimalFormat = NumberFormat.getInstance(Locale.getDefault()) as DecimalFormat
//            formatter.applyPattern("#,###,###,###")
            formatter.applyPattern(pattern)
            val formattedString: String = formatter.format(longVal)

            //setting text after format to EditText
            editText.setText(formattedString)
            editText.setSelection(editText.text.length)
        } catch (nfe: NumberFormatException) {
            nfe.printStackTrace()
        }

        editText.addTextChangedListener(this)
    }
}