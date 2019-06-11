package com.mlsdev.mlsdevstore.presentaion.product

import android.os.Bundle
import android.view.View
import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import com.mlsdev.mlsdevstore.R
import com.mlsdev.mlsdevstore.data.DataSource
import com.mlsdev.mlsdevstore.data.cart.Cart
import com.mlsdev.mlsdevstore.data.model.product.Image
import com.mlsdev.mlsdevstore.data.model.product.Product
import com.mlsdev.mlsdevstore.presentaion.favorites.FavoritesViewModel
import com.mlsdev.mlsdevstore.presentaion.utils.Utils
import kotlinx.coroutines.launch
import javax.inject.Inject

open class ProductDetailsViewModel @Inject
constructor(
        private val dataSource: DataSource,
        private val utils: Utils,
        private val cart: Cart
) : FavoritesViewModel(dataSource), Cart.OnMaxItemsReachedListener, Cart.OnItemAddedListener {

    private var item: Product? = null
    val title = ObservableField<String>()
    val imageUrl = ObservableField<String>()
    val price = ObservableField<String>()
    val currency = ObservableField<String>()
    val feedbackScore = ObservableField("0.0")
    val feedbackPercent = ObservableField("0.0")
    val descriptionStateLiveData = MutableLiveData<Boolean>()
    val description = ObservableField<String>()
    val condition = ObservableField<String>()
    val brand = ObservableField<String>()
    val size = ObservableField<String>()
    val gender = ObservableField<String>()
    val color = ObservableField<String>()
    val material = ObservableField<String>()
    val imagesLiveData = MutableLiveData<List<Image>>()
    private val infoMessageLiveData = MutableLiveData<Int>()
    private val favoriteLiveData = MutableLiveData<Boolean>()

    fun getFavoriteLiveData(): LiveData<Boolean> = favoriteLiveData

    fun getInfoMessageLive(): LiveData<Int> = infoMessageLiveData

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    internal fun start() {
        cart.addOnMaxItemsReachedListener(this)
        cart.addOnItemAddedListener(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    internal fun stop() {
        cart.removeOnMaxItemsReachedListener(this)
        cart.removeOnItemAddedListener(this)
    }

    fun setProductDetailsData(productDetailsData: Bundle?) {
        if (productDetailsData == null)
            return

        ProductFragmentArgs.fromBundle(productDetailsData).productDetails.let {
            item = it
            title.set(it.itemTitle)
            imageUrl.set(it.imageUrl)
            price.set(it.itemPrice.value.toString())
            currency.set(it.itemPrice.currency)
            condition.set(it.itemCondition)
            retrieveDetailedInfo(it.id)
            checkIsFavorite(it.itemId)
        }
    }

    private fun retrieveDetailedInfo(itemId: String) {
        checkNetworkConnection(utils) {
            compositeDisposable.add(dataSource.getItem(itemId).subscribe(
                    { product: Product ->
                        description.set(product.description)
                        brand.set(product.brand)
                        size.set(product.size)
                        gender.set(product.gender)
                        color.set(product.color)
                        material.set(product.material)
                        feedbackScore.set(product.seller?.feedbackScore.toString())
                        feedbackPercent.set(product.seller?.getFeedbackRating())
                        imageUrl.set(product.imageUrl)
                        imageUrl.notifyChange()

                        val images = product.additionalImages
                        if (images.isEmpty() && product.image != null)
                            images.add(product.image ?: Image(null))

                        imagesLiveData.postValue(product.additionalImages)
                    },
                    { handleError(it) }
            ))
        }
    }

    fun onAddToCartClicked(@Suppress("UNUSED_PARAMETER") button: View) {
        cart.addItem(item)
    }

    fun onDescriptionClick() {
        descriptionStateLiveData.postValue(true)
    }

    override fun onMaxItemsReached() {
        infoMessageLiveData.postValue(R.string.message_item_count_restriction)
    }

    override fun onItemAdded(item: Product) {
        infoMessageLiveData.postValue(R.string.message_item_added)
    }

    private fun checkIsFavorite(productId: String) {
        viewModelScope.launch {
            val result = dataSource.isProductFavored(productId)
            item?.isFavorite = result
            favoriteLiveData.postValue(result)
        }
    }

    fun onFavoriteButtonClick() {
        item?.let {
            if (it.isFavorite) removeProductFromFavorites(it)
            else addProductIntoFavorites(it)

            checkIsFavorite(it.itemId)
        }
    }
}
