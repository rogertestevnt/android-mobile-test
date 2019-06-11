package com.mlsdev.mlsdevstore.presentaion.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mlsdev.mlsdevstore.R
import com.mlsdev.mlsdevstore.data.model.product.Product
import com.mlsdev.mlsdevstore.presentaion.adapter.BasePagedAdapter
import com.mlsdev.mlsdevstore.presentaion.adapter.ProductViewHolder

class PagedProductsAdapter(
        retryCallback: () -> Unit,
        private val onItemClickListener: (product: Product) -> Unit
) : BasePagedAdapter<Product>(retryCallback, diffCallback) {
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            R.layout.layout_network_state -> (holder as BasePagedAdapter<*>.NetworkStateViewHolder).bindTo()
            R.layout.item_product -> (holder as ProductViewHolder).bindView(getItem(position))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        R.layout.layout_network_state -> NetworkStateViewHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_network_state, parent, false), retryCallback)
        R.layout.item_product -> ProductViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false),
                onItemClickListener
        )
        else -> throw Exception("Unknown item view type")
    }

    override fun getItemViewType(position: Int): Int = when {
        (hasExtraRow() && position == itemCount - 1) -> R.layout.layout_network_state
        getItem(position) != null -> R.layout.item_product
        else -> throw Exception("Unknown item view type")
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Product>() {
            override fun areItemsTheSame(oldProduct: Product, newProduct: Product): Boolean =
                    oldProduct.id == newProduct.id

            override fun areContentsTheSame(oldProduct: Product, newProduct: Product): Boolean =
                    oldProduct.brand == newProduct.brand &&
                            oldProduct.description == newProduct.description &&
                            oldProduct.itemPrice.value == newProduct.itemPrice.value &&
                            oldProduct.itemTitle == newProduct.itemTitle
        }
    }
}