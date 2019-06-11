package com.mlsdev.mlsdevstore.presentaion.product

import android.annotation.SuppressLint
import androidx.databinding.DataBindingUtil
import android.os.Bundle
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.mlsdev.mlsdevstore.R
import com.mlsdev.mlsdevstore.databinding.FragmentBottomSheetBinding
import com.mlsdev.mlsdevstore.presentaion.utils.ExtrasKeys

class DescriptionBottomSheetFragment : BottomSheetDialogFragment() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val data = arguments
        val binding = DataBindingUtil.inflate<FragmentBottomSheetBinding>(
                inflater,
                R.layout.fragment_bottom_sheet,
                container,
                false)

        if (data != null && data.containsKey(ExtrasKeys.KEY_PRODUCT_DESCRIPTION)) {
            binding.description.settings.javaScriptEnabled = true
            binding.description.loadDataWithBaseURL("", data.getString(ExtrasKeys.KEY_PRODUCT_DESCRIPTION), "text/html", "UTF-8", "")
        }

        return binding.root
    }
}
