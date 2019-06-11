package com.mlsdev.mlsdevstore.presentation.cart

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.mlsdev.mlsdevstore.data.cart.Cart
import com.mlsdev.mlsdevstore.data.model.product.ListItem
import com.mlsdev.mlsdevstore.data.model.product.Product
import com.mlsdev.mlsdevstore.data.remote.datasource.getPagingConfig
import com.mlsdev.mlsdevstore.data.repository.CartProductsRepository
import com.mlsdev.mlsdevstore.presentaion.cart.CartViewModel
import com.mlsdev.mlsdevstore.stub.data.local.CartItemsDataSourceFactoryStub
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CartViewModelTest {

    @Mock
    lateinit var repository: CartProductsRepository

    @Mock
    lateinit var cart: Cart

    lateinit var viewModel: CartViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun beforeTest() {
        MockitoAnnotations.initMocks(this)
        viewModel = CartViewModel(cart, repository)
    }

    @Test
    fun observeProductsInCart() {
        val product1 = Product()
        val product2 = Product()
        val totalSum = 1.1

        product1.itemId = "id1"
        product2.itemId = "id2"
        product1.title = "title1"
        product2.title = "title2"

        val products = arrayListOf(product1, product2)
        val pagedListLiveDataStub = LivePagedListBuilder(CartItemsDataSourceFactoryStub(products, totalSum), getPagingConfig()).build()
        val productsObserver = Observer<PagedList<ListItem>> {
            Assert.assertEquals(3, it.size)
            Assert.assertEquals(product1.id, it[0]?.id)
            Assert.assertEquals(product2.id, it[1]?.id)
            Assert.assertEquals(totalSum, it[2]?.itemPrice?.value)
            viewModel.cartIsEmpty.set(it.isEmpty())
        }
        Mockito.`when`(repository.getProducts()).thenReturn(pagedListLiveDataStub)

        viewModel.productsInCart.observeForever(productsObserver)

        Assert.assertEquals(false, viewModel.cartIsEmpty.get())
    }

    @Test
    fun observeProductsInCart_EmptyCart() {
        val products = emptyList<Product>()
        val pagedListLiveDataStub = LivePagedListBuilder(CartItemsDataSourceFactoryStub(products, 0.0), getPagingConfig()).build()
        val productsObserver = Observer<PagedList<ListItem>> { viewModel.cartIsEmpty.set(it.isEmpty()) }
        Mockito.`when`(repository.getProducts()).thenReturn(pagedListLiveDataStub)

        viewModel.productsInCart.observeForever(productsObserver)

        Assert.assertEquals(true, viewModel.cartIsEmpty.get())
    }

}