package com.mlsdev.mlsdevstore.presentaion.cart

import androidx.databinding.ObservableField

import java.text.DecimalFormat

class TotalSumItemViewModel() {
    val totalSum = ObservableField("00.00")

    fun setTotalSum(totalSum: Double) {
        this.totalSum.set(DecimalFormat("#0.00").format(totalSum))
    }
}
