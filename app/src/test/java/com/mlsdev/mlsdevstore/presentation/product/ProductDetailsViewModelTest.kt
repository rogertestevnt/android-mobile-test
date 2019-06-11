package com.mlsdev.mlsdevstore.presentation.product

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mlsdev.mlsdevstore.data.DataSource
import com.mlsdev.mlsdevstore.data.cart.Cart
import com.mlsdev.mlsdevstore.data.model.product.Image
import com.mlsdev.mlsdevstore.data.model.product.Price
import com.mlsdev.mlsdevstore.data.model.product.Product
import com.mlsdev.mlsdevstore.presentaion.product.ProductDetailsViewModel
import com.mlsdev.mlsdevstore.presentaion.utils.Utils
import io.reactivex.Single
import okhttp3.ResponseBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.HttpException
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class ProductDetailsViewModelTest {

    @Mock
    lateinit var dataSource: DataSource

    @Mock
    lateinit var utils: Utils

    @Mock
    lateinit var cart: Cart

    @Mock
    lateinit var productBundle: Bundle

    private val bundleKey = "product_details"
    lateinit var product: Product
    private lateinit var viewModel: ProductDetailsViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun beforeTest() {
        MockitoAnnotations.initMocks(this)
        product = Product()
        product.itemId = "id"
        product.title = "Some favorite title"
        product.price = Price(1.1, "USD")
        product.image = Image("https://image.url")
        Mockito.`when`(productBundle.containsKey(bundleKey)).thenReturn(true)
        Mockito.`when`(productBundle.get(bundleKey)).thenReturn(product)
        viewModel = ProductDetailsViewModel(dataSource, utils, cart)
    }

    @Test
    fun setProductDetailsData() {
        Mockito.`when`(dataSource.getItem(product.id)).thenReturn(Single.just(product))
        Mockito.`when`(utils.isNetworkAvailable()).thenReturn(true)

        viewModel.setProductDetailsData(productBundle)

        Mockito.verify(productBundle).containsKey(bundleKey)
        Mockito.verify(productBundle).get(bundleKey)
        Mockito.verify(dataSource).getItem(product.id)
        Assert.assertEquals(product.title, viewModel.title.get())
        Assert.assertEquals(product.imageUrl, viewModel.imageUrl.get())
        Assert.assertEquals(product.itemPrice.value.toString(), viewModel.price.get())
        Assert.assertEquals(product.itemPrice.currency, viewModel.currency.get())
    }

    @Test
    fun setProductDetailsData_NoNetworkConnection() {
        Mockito.`when`(utils.isNetworkAvailable()).thenReturn(false)

        viewModel.setProductDetailsData(productBundle)

        Mockito.verify<DataSource>(dataSource, Mockito.never()).getItem(product.itemId)
    }

    @Test
    fun setProductDetailsData_DataIsNull() {
        viewModel.setProductDetailsData(null)

        Assert.assertNull(viewModel.title.get())
        Assert.assertNull(viewModel.imageUrl.get())
        Assert.assertNull(viewModel.price.get())
        Assert.assertNull(viewModel.currency.get())
        Mockito.verify<DataSource>(dataSource, Mockito.never()).getItem(ArgumentMatchers.anyString())
    }

    @Test
    fun getProductDetails_ProductWasNotFound() {
        val httpExceptionMock = Mockito.mock(HttpException::class.java)
        val response = Response.error<Any>(404, ResponseBody.create(null, ""))
        Mockito.`when`(httpExceptionMock.code()).thenReturn(404)
        Mockito.`when`(httpExceptionMock.response()).thenReturn(response)
        Mockito.`when`(utils.isNetworkAvailable()).thenReturn(true)
        Mockito.`when`(dataSource.getItem(product.itemId)).thenReturn(Single.error(httpExceptionMock))

        viewModel.setProductDetailsData(productBundle)

        Mockito.verify(dataSource).getItem(product.itemId)
        Assert.assertEquals(true, viewModel.commonErrorLiveData.value)
    }

}