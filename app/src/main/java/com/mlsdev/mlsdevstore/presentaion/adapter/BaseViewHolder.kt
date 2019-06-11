package com.mlsdev.mlsdevstore.presentaion.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.View

abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {

    abstract fun bindView(item: T?)
}
