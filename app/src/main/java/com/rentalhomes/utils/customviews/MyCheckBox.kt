package com.rentalhomes.utils.customviews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox

class MyCheckBox : AppCompatCheckBox {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        init()
        /* String str = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "font");

        String fontName = "";
        try {
            fontName = context.getString(Integer.parseInt(str.substring(1).trim()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        setFont(context, fontName);*/
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun setFont(context: Context, fontName: String) {
        val font = Typeface.createFromAsset(context.assets, fontName)
        setTypeface(font)
    }

    fun resizeAndSetText(text: String, maxLength: Int) {
        var text = text
        if (text.length > maxLength) {
            text = text.substring(0, maxLength) + "..."
        }
        text = text
    }

    private fun init() {
//        During project initiation change the fonts_name.ttf 
        val typeface: Typeface = Typeface.createFromAsset(context.assets, "fonts/fonts_name.ttf")
        setTypeface(typeface)
    }
}