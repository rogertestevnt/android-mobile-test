package com.mlsdev.mlsdevstore.presentaion.utils

import androidx.databinding.ObservableBoolean

class CustomObservableBoolean : ObservableBoolean {

    private var value: Boolean = false

    constructor(value: Boolean) {
        this.value = value
    }

    constructor()

    override fun get(): Boolean {
        return this.value
    }

    override fun set(value: Boolean) {
        this.value = value
        notifyChange()
    }
}
