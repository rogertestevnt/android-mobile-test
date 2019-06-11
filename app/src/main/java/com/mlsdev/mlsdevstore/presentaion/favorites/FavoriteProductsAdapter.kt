package com.mlsdev.mlsdevstore.presentaion.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mlsdev.mlsdevstore.R
import com.mlsdev.mlsdevstore.data.model.product.Product
import com.mlsdev.mlsdevstore.presentaion.adapter.FavoriteProductViewHolder

class FavoriteProductsAdapter(
        private val onItemClickListener: (product: Product) -> Unit,
        private val addToFavoritesListener: (product: Product) -> Unit,
        private val removeToFavoritesListener: (product: Product) -> Unit
) : RecyclerView.Adapter<FavoriteProductViewHolder>() {
    private val items = ArrayList<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteProductViewHolder =
            FavoriteProductViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false),
                    onItemClickListener,
                    addToFavoritesListener,
                    removeToFavoritesListener)

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: FavoriteProductViewHolder, position: Int) {
        if (items.size > position)
            holder.bindView(items[position])
    }

    fun setItems(items: List<Product>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

}