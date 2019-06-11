package com.mlsdev.mlsdevstore.presentation.products

import android.os.Bundle
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import androidx.paging.RxPagedListBuilder
import com.mlsdev.mlsdevstore.data.DataLoadState
import com.mlsdev.mlsdevstore.data.model.image.CategoryImageEntity
import com.mlsdev.mlsdevstore.data.model.product.Product
import com.mlsdev.mlsdevstore.data.remote.datasource.getPagingConfig
import com.mlsdev.mlsdevstore.data.repository.ProductsRepository
import com.mlsdev.mlsdevstore.data.repository.SearchImageRepository
import com.mlsdev.mlsdevstore.presentaion.products.ProductsViewModel
import com.mlsdev.mlsdevstore.presentaion.utils.ExtrasKeys
import com.mlsdev.mlsdevstore.presentaion.utils.Utils
import com.mlsdev.mlsdevstore.stub.data.remote.datasource.ProductsDataSourceFactoryStub
import io.reactivex.Observable
import io.reactivex.Single
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
import retrofit2.HttpException

@RunWith(MockitoJUnitRunner::class)
class ProductsViewModelTest {

    @Mock
    lateinit var utils: Utils

    @Mock
    lateinit var productsRepository: ProductsRepository

    @Mock
    lateinit var imagesRepository: SearchImageRepository

    @Mock
    lateinit var loadingStateObserver: Observer<DataLoadState>

    @Mock
    lateinit var categoryData: Bundle

    lateinit var viewModel: ProductsViewModel
    lateinit var categoryImage: CategoryImageEntity
    lateinit var observablePagedList: Observable<PagedList<Product>>
    private val categoryId = "id"
    private val categoryName = "name"
    private val imageUrl = "image/url"

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun beforeTest() {
        MockitoAnnotations.initMocks(this)
        categoryImage = CategoryImageEntity(categoryId, imageUrl)
        viewModel = ProductsViewModel(utils, productsRepository, imagesRepository)
    }

    private fun mockCategoryData() {
        observablePagedList = RxPagedListBuilder(ProductsDataSourceFactoryStub(), getPagingConfig()).buildObservable()
        Mockito.`when`(categoryData.getString(ExtrasKeys.KEY_CATEGORY_ID)).thenReturn(categoryId)
        Mockito.`when`(categoryData.getString(ExtrasKeys.KEY_CATEGORY_NAME, "")).thenReturn(categoryName)
        Mockito.`when`(utils.isNetworkAvailable()).thenReturn(true)
        Mockito.`when`(imagesRepository.searchImage(categoryId, categoryName)).thenReturn(Single.just(categoryImage))
        Mockito.`when`(productsRepository.getItems(categoryId)).thenReturn(observablePagedList)
    }

    @Test
    fun initCategoryData() {
        mockCategoryData()
        Mockito.`when`(categoryData.getString(ExtrasKeys.KEY_CATEGORY_NAME, "")).thenReturn(null)

        viewModel.initCategoryData(categoryData)
        viewModel.products.test()
                .assertSubscribed()
                .assertValueAt(0, observablePagedList.blockingFirst())
                .assertNoErrors()
                .dispose()

        Assert.assertEquals("", viewModel.categoryName.get())
        Assert.assertNull(viewModel.categoryImage.get())
        Mockito.verify(utils, Mockito.times(1)).isNetworkAvailable()
        Mockito.verify(productsRepository).getItems(categoryId)
        Mockito.verify(imagesRepository, Mockito.never()).searchImage(categoryId, "")
    }

    @Test
    fun initCategoryData_CategoryNameNull() {
        mockCategoryData()

        viewModel.initCategoryData(categoryData)
        viewModel.products.test()
                .assertSubscribed()
                .assertValueAt(0, observablePagedList.blockingFirst())
                .assertNoErrors()
                .dispose()

        Assert.assertEquals(categoryName, viewModel.categoryName.get())
        Assert.assertEquals(imageUrl, viewModel.categoryImage.get())
        Mockito.verify(utils, Mockito.times(2)).isNetworkAvailable()
        Mockito.verify(productsRepository).getItems(categoryId)
        Mockito.verify(imagesRepository).searchImage(categoryId, categoryName)
    }

    @Test
    fun initCategoryData_LoadImageError() {
        mockCategoryData()
        val httpExceptionMock = Mockito.mock(HttpException::class.java)
        Mockito.`when`(imagesRepository.searchImage(categoryId, categoryName)).thenReturn(Single.error(httpExceptionMock))

        viewModel.initCategoryData(categoryData)
        viewModel.products.test()
                .assertSubscribed()
                .assertValueAt(0, observablePagedList.blockingFirst())
                .assertNoErrors()
                .dispose()

        Assert.assertEquals(categoryName, viewModel.categoryName.get())
        Assert.assertNull(viewModel.categoryImage.get())
        Mockito.verify(utils, Mockito.times(2)).isNetworkAvailable()
        Mockito.verify(productsRepository).getItems(categoryId)
        Mockito.verify(imagesRepository).searchImage(categoryId, categoryName)
    }

    @Test(expected = Exception::class)
    fun initCategoryData_CategoryIdNull() {
        mockCategoryData()
        Mockito.`when`(categoryData.getString(ExtrasKeys.KEY_CATEGORY_ID)).thenReturn(null)

        viewModel.initCategoryData(categoryData)

        Assert.assertEquals(categoryName, viewModel.categoryName.get())
        Assert.assertEquals(imageUrl, viewModel.categoryImage.get())
        Mockito.verify(utils, Mockito.times(1)).isNetworkAvailable()
        Mockito.verify(productsRepository, Mockito.never()).getItems(categoryId)
        Mockito.verify(imagesRepository, Mockito.never()).searchImage(categoryId, categoryName)
    }

    @Test
    fun observeLoadingState() {
        Mockito.`when`(productsRepository.getPageLoadingState()).thenReturn(MutableLiveData<DataLoadState>(DataLoadState.LOADING))
        viewModel.loadingState.observeForever(loadingStateObserver)

        Mockito.verify(loadingStateObserver).onChanged(DataLoadState.LOADING)
        Assert.assertEquals(DataLoadState.LOADING, viewModel.loadingState.value)
    }

    @Test
    fun refresh() {
        Mockito.doNothing().`when`(productsRepository).refresh()
        Mockito.`when`(utils.isNetworkAvailable()).thenReturn(true)

        viewModel.refresh()

        Mockito.verify(productsRepository).refresh()
    }

    @Test
    fun retry() {
        Mockito.doNothing().`when`(productsRepository).retry()
        Mockito.`when`(utils.isNetworkAvailable()).thenReturn(true)

        viewModel.retry()

        Mockito.verify(productsRepository).retry()
    }

}