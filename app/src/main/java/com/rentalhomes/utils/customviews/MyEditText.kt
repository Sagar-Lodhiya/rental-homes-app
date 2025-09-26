package com.rentalhomes.utils.customviews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class MyEditText : AppCompatEditText {
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
        /*val str = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "font")
        var fontName = ""
        try {
            fontName = context.getString(str.substring(1).trim { it <= ' ' }.toInt())
        } catch (e: Exception) {
            e.printStackTrace()
        }
        setFont(context, fontName)*/
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    constructor(context: Context) : super(context) {
        init()
    }

    private fun setFont(context: Context, fontName: String) {
        val font = Typeface.createFromAsset(context.assets, fontName)
        typeface = font
    }

    fun resizeAndSetText(text: String, maxLength: Int) {
        var text = text
        if (text.length > maxLength) {
            text = text.substring(0, maxLength) + "..."
        }
        this.setText(text)
    }

    private fun init() {
//        During project initiation change the fonts_name.ttf
        val typeface: Typeface = Typeface.createFromAsset(context.assets, "fonts/fonts_name.ttf")
        setTypeface(typeface)
    }
}