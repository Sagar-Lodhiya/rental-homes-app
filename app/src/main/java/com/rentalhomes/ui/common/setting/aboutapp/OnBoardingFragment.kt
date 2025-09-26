package com.rentalhomes.ui.common.setting.aboutapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.rentalhomes.R
import com.rentalhomes.databinding.FragmentOnBoardingBinding

class OnBoardingFragment : Fragment() {
    lateinit var binding: FragmentOnBoardingBinding

    interface OnBoardingListener {
        fun onNextClick()
    }

    companion object {
        const val IMAGE = "image"

        /**
         * @param name a string to be displayed on the fragment
         * @param listener click listener to pass click events to the activity
         */
        fun newInstance(image: Int, listener: OnBoardingListener): Fragment {
            val fragment = OnBoardingFragment()
            val bundle = Bundle()
            bundle.putInt(IMAGE, image)
            fragment.arguments = bundle
            fragment.onBoardingListener = listener
            return fragment
        }
    }

    private lateinit var onBoardingListener: OnBoardingListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_on_boarding, container, false)
        arguments?.let { binding.ivOnBoarding.setImageResource(it.getInt(IMAGE)) }
        // pass the click event to the activity
        return binding.root
    }

}