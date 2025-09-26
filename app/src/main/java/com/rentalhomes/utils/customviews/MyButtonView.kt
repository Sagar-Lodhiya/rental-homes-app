package com.rentalhomes.utils.customviews

import android.content.Context
import androidx.appcompat.widget.AppCompatButton
import android.graphics.Typeface
import android.util.AttributeSet

class MyButtonView : AppCompatButton {
    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!, attrs
    ) {
        init()
        /*String str = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "font");

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
        typeface = font
    }

    fun resizeAndSetText(text: String, maxLength: Int) {
        var text = text
        if (text.length > maxLength) {
            text = text.substring(0, maxLength) + "..."
        }
        text = text
    }

    fun init() {
//        During project initiation change the fonts_name.ttf 
        val typeface = Typeface.createFromAsset(context.assets, "fonts/fonts_name.ttf")
        setTypeface(typeface)
    }
}