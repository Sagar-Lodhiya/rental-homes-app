package com.rentalhomes.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.CommonUtils
import com.rentalhomes.utils.NetworkUtils.isInternetAvailable
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

abstract class BaseFragment<T : ViewDataBinding?, V : BaseViewModel<*>?> : Fragment() {

    private var mRootView: View? = null
    private var mViewDataBinding: T? = null
    private var mViewModel: V? = null

    /**
     * Override for set binding variable
     *
     * @return variable id
     */
    abstract fun getBindingVariable(): Int

    /**
     * @return layout resource id
     */
    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun getViewModel(): V

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = getViewModel()
//        setHasOptionsMenu(false);
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        mRootView = mViewDataBinding?.root
        return mRootView
    }

    open fun showInternetAlert(isConnected: Boolean) {
        if (!isConnected) {
            AlertDialogUtils.showInternetAlert(activity)
        }
    }

    open fun showProgressDialog() {
        if (isInternetAvailable(requireActivity())) {
            CommonUtils.showProgressDialog(requireActivity())
        }
    }

    open fun hideProgressDialog() {
        if (isInternetAvailable(requireActivity())) {
            CommonUtils.hideProgressDialog()
        }
    }

    open fun getViewDataBinding(): T {
        return mViewDataBinding!!
    }

    open fun numberFormat(value: Int): String? {
        val longVal: Long = value.toLong()
        val formatter: DecimalFormat =
            NumberFormat.getInstance(Locale.US) as DecimalFormat
        formatter.applyPattern("#,###,###,###")
        return formatter.format(longVal)
    }
}