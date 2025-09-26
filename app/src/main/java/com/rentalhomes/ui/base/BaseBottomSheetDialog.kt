package com.rentalhomes.ui.base

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rentalhomes.utils.AlertDialogUtils
import com.rentalhomes.utils.CommonUtils
import com.rentalhomes.utils.NetworkUtils

abstract class BaseBottomSheetDialog<T : ViewDataBinding?, V : BaseViewModel<*>?> :
    BottomSheetDialogFragment() {

    private var mRootView: View? = null
    private var mViewDataBinding: T? = null
    private var mViewModel: V? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (requireView().parent as View).setBackgroundColor(Color.TRANSPARENT)
        mViewModel = getViewModel()
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

    open fun getViewDataBinding(): T {
        return mViewDataBinding!!
    }

    fun showInternetAlert(isConnected: Boolean) {
        if (!isConnected) {
            AlertDialogUtils.showInternetAlert(requireActivity())
        }
    }

    fun showProgressDialog() {
        if (NetworkUtils.isInternetAvailable(requireActivity())) {
            CommonUtils.showProgressDialog(requireActivity())
        }
    }

    fun hideProgressDialog() {
        CommonUtils.hideProgressDialog()
    }
}