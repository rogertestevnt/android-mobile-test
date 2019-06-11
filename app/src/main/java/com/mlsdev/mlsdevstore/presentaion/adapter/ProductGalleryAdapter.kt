package com.mlsdev.mlsdevstore.presentaion.adapter

import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.mlsdev.mlsdevstore.R
import com.mlsdev.mlsdevstore.data.model.product.Image
import com.mlsdev.mlsdevstore.databinding.LayoutGalleryItemBinding

import java.util.ArrayList

import javax.inject.Inject

class ProductGalleryAdapter @Inject
constructor() : PagerAdapter() {
    private var images: List<Image> = ArrayList()

    fun setImages(images: List<Image>) {
        this.images = images
        notifyDataSetChanged()
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val image: Image? = images[position]

        val binding = DataBindingUtil.inflate<LayoutGalleryItemBinding>(
                LayoutInflater.from(container.context),
                R.layout.layout_gallery_item,
                container,
                false)

        binding.imageUrl = image?.imageUrl

        container.addView(binding.root)
        return binding.root
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}
