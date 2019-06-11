package com.mlsdev.mlsdevstore.presentaion.bottom_navigation

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import androidx.databinding.ObservableInt

import com.mlsdev.mlsdevstore.data.cart.Cart
import com.mlsdev.mlsdevstore.presentaion.viewmodel.BaseViewModel

import javax.inject.Inject

class BottomNavigationViewModel @Inject
constructor(private val cart: Cart) : BaseViewModel(), Cart.OnItemCountChangeListener {
    val itemCount = ObservableInt(0)

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onStart() {
        cart.addOnItemCountChangeListener(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        cart.removeOnItemCountChangeListener(this)
    }

    override fun onItemCountChanged(count: Int) {
        itemCount.set(count)
    }
}
