package com.mlsdev.mlsdevstore.presentaion.adapter

import android.view.View
import com.mlsdev.mlsdevstore.data.model.product.ListItem
import com.mlsdev.mlsdevstore.data.model.product.Product
import com.mlsdev.mlsdevstore.databinding.ItemProductBinding
import com.mlsdev.mlsdevstore.presentaion.store.ProductItemViewModel

open class ProductViewHolder(
        itemView: View,
        val onItemClickListener: (product: Product) -> Unit
) : BaseViewHolder<Product>(itemView) {
    protected val binding = ItemProductBinding.bind(itemView)

    override fun bindView(item: Product?) {
        val viewModel = ProductItemViewModel(null)
        viewModel.setItem(item as ListItem)
        binding.viewModel = viewModel
        binding.root.setOnClickListener { onItemClickListener(item) }
    }
}