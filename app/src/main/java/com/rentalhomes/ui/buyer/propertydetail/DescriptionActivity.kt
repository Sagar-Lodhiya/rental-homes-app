package com.rentalhomes.ui.buyer.propertydetail

import android.os.Bundle
import androidx.appcompat.widget.AppCompatImageView
import com.rentalhomes.R
import com.rentalhomes.data.network.Keys
import com.rentalhomes.utils.customviews.RegularTextView
import com.yanzhenjie.album.mvp.BaseActivity

class DescriptionActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)
        val tvDescription = findViewById<RegularTextView>(R.id.tvDesc)
        val btnBack = findViewById<AppCompatImageView>(R.id.ivBack)
        tvDescription.text = intent.getStringExtra(Keys.DESCRIPTION)!!
        btnBack.setOnClickListener {
            finish()
        }
    }
}