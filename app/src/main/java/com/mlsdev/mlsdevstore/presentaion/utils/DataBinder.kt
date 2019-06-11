package com.mlsdev.mlsdevstore.presentaion.utils

import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.mlsdev.mlsdevstore.R
import com.squareup.picasso.Picasso

object DataBinder {

    @BindingAdapter("imageUrl")
    @JvmStatic
    fun setImageUrl(imageView: ImageView, imageUrl: String?) {
        if (imageUrl.isNullOrBlank()) return

        Picasso.with(imageView.context)
                .load(imageUrl)
                .error(R.drawable.bg_splash_screen)
                .placeholder(R.drawable.bg_splash_screen)
                .into(imageView)
    }

    @BindingAdapter("categoryImage")
    @JvmStatic
    fun setCategoryImage(imageView: ImageView, categoryImage: String?) {
        Picasso.with(imageView.context)
                .load(categoryImage)
                .into(imageView)
    }

    @BindingAdapter("error")
    @JvmStatic
    fun setError(inputLayout: TextInputLayout, error: String?) {
        inputLayout.isErrorEnabled = true
        inputLayout.error = error
    }

    @BindingAdapter("cardIcon")
    @JvmStatic
    fun setCardIcon(imageView: AppCompatImageView, cardType: CreditCardTypeDetector.Type?) {
        if (cardType == null || cardType == CreditCardTypeDetector.Type.UNKNOWN) {
            imageView.setImageDrawable(null)
            return
        }

        imageView.setImageResource(when (cardType) {
            CreditCardTypeDetector.Type.UNKNOWN -> android.R.color.transparent
            CreditCardTypeDetector.Type.VISA -> R.drawable.ic_visa
            CreditCardTypeDetector.Type.MASTERCARD -> R.drawable.ic_mastercard
            CreditCardTypeDetector.Type.AMERICAN_EXPRESS -> R.drawable.ic_amex
            CreditCardTypeDetector.Type.DISCOVER -> R.drawable.ic_discover
        })
    }

}
