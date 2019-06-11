package com.mlsdev.mlsdevstore.presentaion.viewmodel

import androidx.databinding.ObservableField

class BaseHeaderViewModel {

    var header = ObservableField<String>()

    fun setHeader(header: String) {
        this.header.set(header)
    }

}
