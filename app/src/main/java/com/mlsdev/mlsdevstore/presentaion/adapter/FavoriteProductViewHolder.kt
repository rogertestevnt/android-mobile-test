package com.mlsdev.mlsdevstore.presentaion.adapter

import android.view.View
import com.mlsdev.mlsdevstore.data.model.product.Product

class FavoriteProductViewHolder(
        itemView: View,
        onItemClickListener: (product: Product) -> Unit,
        private val addToFavoritesListener: (product: Product) -> Unit,
        private val removeToFavoritesListener: (product: Product) -> Unit
) : ProductViewHolder(itemView, onItemClickListener) {

    override fun bindView(item: Product?) {
        super.bindView(item)
    }

}