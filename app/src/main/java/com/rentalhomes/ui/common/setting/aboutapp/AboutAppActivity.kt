package com.rentalhomes.ui.common.setting.aboutapp

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.rentalhomes.BR
import com.rentalhomes.R
import com.rentalhomes.databinding.ActivityAboutAppBinding
import com.rentalhomes.ui.base.BaseActivity
import com.rentalhomes.utils.GlobalMethods

/**
 * Created by Dharmesh
 * */
class AboutAppActivity : BaseActivity<ActivityAboutAppBinding, AboutAppVM>(),
    AboutAppNavigator, OnBoardingFragment.OnBoardingListener {
    private lateinit var binding: ActivityAboutAppBinding
    private lateinit var aboutAppVM: AboutAppVM
    private lateinit var snackBar: View

    private var currentPage = 0
    private val fragments = ArrayList<Fragment>()

    private var dotsCount: Int = 6
    private var dots: ArrayList<ImageView> = ArrayList(6)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
        setHeader()
        setOnListener()
    }

    private fun setOnListener() {
        binding.headerBar.ivBack.setOnClickListener {
            onBackPressed()
        }

        binding.viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (dotsCount != 0) {
                    for (i in 0 until dotsCount) {
                        dots[i].setImageResource(R.drawable.ic_indicator_unselected)
                    }
                    dots[position].setImageResource(R.drawable.ic_indicator_selected)
                }

                setupIndicator()
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })
    }

    //Initialization is done here
    private fun init() {
        //Set status bar color
        GlobalMethods.setStatusBarCustomColor(this, R.color.white)

        binding = getViewDataBinding()
        binding.let {
            it.lifecycleOwner = this
            it.aboutAppVM = aboutAppVM
        }
        aboutAppVM.attachContext(this)
        aboutAppVM.setNavigator(this)

        //Initialize snackBar object here
        snackBar = findViewById(android.R.id.content)

        //Set Adapter
        val adapter = OnBoardingViewPagerAdapter(supportFragmentManager)
        fragments.add(OnBoardingFragment.newInstance(R.drawable.logo, this))
        fragments.add(OnBoardingFragment.newInstance(R.drawable.logo, this))
        fragments.add(OnBoardingFragment.newInstance(R.drawable.logo, this))
        fragments.add(OnBoardingFragment.newInstance(R.drawable.logo, this))
        fragments.add(OnBoardingFragment.newInstance(R.drawable.logo, this))
        fragments.add(OnBoardingFragment.newInstance(R.drawable.logo, this))
        adapter.addFragments(fragments)
        binding.viewPager.adapter = adapter

        dotsCount = 6
        binding.viewPager.offscreenPageLimit = 6
        binding.llIndicator.removeAllViews()
        dots.add(ImageView(this))
        dots.add(ImageView(this))
        dots.add(ImageView(this))
        dots.add(ImageView(this))
        dots.add(ImageView(this))
        dots.add(ImageView(this))

        if (dotsCount != 0) {
            for (i in 0 until dotsCount) {
                dots[i].setImageResource(R.drawable.ic_indicator_unselected)
            }
            dots[0].setImageResource(R.drawable.ic_indicator_selected)
        }

        setupIndicator()
    }

    //Data binding
    override val bindingVariable: Int
        get() = BR.aboutAppVM
    override val layoutId: Int
        get() = R.layout.activity_about_app
    override val viewModel: AboutAppVM
        get() {
            aboutAppVM = ViewModelProvider(this).get(AboutAppVM::class.java)
            return aboutAppVM
        }

    override fun setHeader() {
        binding.headerBar.tvTitle.text =
            resources.getString(R.string.about) + " " + resources.getString(R.string.app_name)
    }

    override fun onNextClick() {
        if (currentPage == fragments.size - 1) {
            finish()
        } else {
            binding.viewPager.setCurrentItem(currentPage + 1, true)
        }
    }

    override fun setObservers() {
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
    }

    private fun setupIndicator() {
        binding.llIndicator.removeAllViews()
        for (i in 0 until dots.size) {
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(8, 0, 8, 0)
            binding.llIndicator.addView(dots[i], params)
        }
    }
}