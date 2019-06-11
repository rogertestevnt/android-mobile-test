package com.mlsdev.mlsdevstore.presentaion.store

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.mlsdev.mlsdevstore.R
import com.mlsdev.mlsdevstore.data.model.category.CategoryTreeNode
import com.mlsdev.mlsdevstore.databinding.ItemCategoryBinding
import com.mlsdev.mlsdevstore.presentaion.adapter.BaseViewHolder
import java.util.*

class CategoriesAdapter(
        val onItemClick: (item: CategoryTreeNode, itemView: View) -> Unit
) : RecyclerView.Adapter<BaseViewHolder<CategoryTreeNode>>() {

    private val categoryTreeNodes = ArrayList<CategoryTreeNode>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemCategoryBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_category,
                parent,
                false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseViewHolder<CategoryTreeNode>, position: Int) {
        holder.bindView(categoryTreeNodes[position])
    }

    override fun getItemCount(): Int {
        return categoryTreeNodes.size
    }

    inner class ViewHolder(private val binding: ItemCategoryBinding) : BaseViewHolder<CategoryTreeNode>(binding.root) {
        override fun bindView(item: CategoryTreeNode?) {
            binding.textCategoryName.setOnClickListener {
                item?.let { categoryTreeNode -> onItemClick(categoryTreeNode, binding.textCategoryName) }
            }

            binding.textCategoryName.text = item?.category?.categoryName
            binding.textCategoryName.transitionName = item?.category?.categoryName
        }
    }

    fun setData(categoryTreeNodes: List<CategoryTreeNode>) {
        this.categoryTreeNodes.clear()
        this.categoryTreeNodes.addAll(categoryTreeNodes)
    }

}
