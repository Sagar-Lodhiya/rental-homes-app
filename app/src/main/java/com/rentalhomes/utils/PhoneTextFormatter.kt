package com.rentalhomes.utils

import android.text.Editable
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.text.TextWatcher
import android.widget.EditText
import timber.log.Timber

class PhoneTextFormatter(editText: EditText, pattern: String): TextWatcher {
    private val TAG = this.javaClass.simpleName

    private var mEditText: EditText = editText

    private var mPattern: String? = null

    init {
        mPattern = pattern
        val maxLength = pattern.length
        mEditText.filters = arrayOf<InputFilter>(LengthFilter(maxLength))
    }





    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        val phone = StringBuilder(s)
        Timber.d("join")
        if (count > 0 && !isValid(phone.toString())) {
            for (i in 0 until phone.length) {
                Timber.d("%s", phone)
                val c = mPattern!![i]
                if (c != '#' && c != phone[i]) {
                    phone.insert(i, c)
                }
            }
            mEditText!!.setText(phone)
            mEditText!!.setSelection(mEditText!!.text.length)
        }

    }

    override fun afterTextChanged(s: Editable?) {

    }

    private fun isValid(phone: String): Boolean {
        for (i in 0 until phone.length) {
            val c = mPattern!![i]
            if (c == '#') continue
            if (c != phone[i]) {
                return false
            }
        }
        return true
    }

}