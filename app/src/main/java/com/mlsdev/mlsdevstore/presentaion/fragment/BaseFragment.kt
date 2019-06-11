package com.mlsdev.mlsdevstore.presentaion.fragment

import android.os.Bundle
import androidx.annotation.StringRes
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.mlsdev.mlsdevstore.presentaion.BaseActivity
import com.mlsdev.mlsdevstore.presentaion.ErrorInViewHandler
import dagger.android.support.DaggerFragment
import javax.inject.Inject

open class BaseFragment : DaggerFragment(), LifecycleOwner {

    @Inject
    lateinit var errorInViewHandler: ErrorInViewHandler

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        errorInViewHandler.setContext(activity!!)
    }

    protected fun setTitle(@StringRes title: Int) {
        (activity as BaseActivity).setToolBarTitle(title)
    }
}
