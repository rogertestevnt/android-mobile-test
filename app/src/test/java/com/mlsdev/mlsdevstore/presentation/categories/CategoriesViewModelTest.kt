package com.mlsdev.mlsdevstore.presentation.categories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.mlsdev.mlsdevstore.data.local.LocalDataSource
import com.mlsdev.mlsdevstore.data.model.category.CategoryTree
import com.mlsdev.mlsdevstore.data.model.category.CategoryTreeNode
import com.mlsdev.mlsdevstore.presentaion.categories.CategoriesViewModel
import com.mlsdev.mlsdevstore.presentaion.utils.Utils
import com.mlsdev.mlsdevstore.presentaion.viewmodel.BaseViewModel
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
import java.net.SocketTimeoutException

@RunWith(MockitoJUnitRunner::class)
class CategoriesViewModelTest : BaseViewModel() {

    @Mock
    lateinit var utils: Utils

    @Mock
    lateinit var dataSource: LocalDataSource

    @Mock
    lateinit var observer: Observer<List<CategoryTreeNode>>

    lateinit var viewModel: CategoriesViewModel

    private val categoryTree = CategoryTree()
    private val categoryTreeNode = CategoryTreeNode()
    private val childCategoryTreeNode = CategoryTreeNode()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()


    @Before
    fun beforeTest() {
        MockitoAnnotations.initMocks(this)
        childCategoryTreeNode.id = 1
        categoryTree.categoryTreeNode = categoryTreeNode
        categoryTreeNode.childCategoryTreeNodes = listOf(childCategoryTreeNode)
        Mockito.`when`(dataSource.loadRootCategoryTree()).thenReturn(Single.just(categoryTree))
        viewModel = Mockito.spy(CategoriesViewModel(dataSource, utils))
    }

    @Test
    fun observeCategories() {
        Mockito.`when`(utils.isNetworkAvailable()).thenReturn(true)
        viewModel.getCategories().observeForever(observer)
        Mockito.verify(observer).onChanged(arrayListOf(childCategoryTreeNode))
        Assert.assertEquals(arrayListOf(childCategoryTreeNode), viewModel.getCategories().value)
    }

    @Test
    fun observeCategories_NoNetworkConnection() {
        Mockito.`when`(utils.isNetworkAvailable()).thenReturn(false)
        viewModel.getCategories().observeForever(observer)
        Mockito.verify(observer, Mockito.never()).onChanged(arrayListOf(childCategoryTreeNode))
        Assert.assertEquals(true, viewModel.networkErrorLiveData.value)
        Assert.assertNull(viewModel.getCategories().value)
    }

    @Test
    fun observeCategories_NotAuthorizedError() {
        val httpExceptionMock = Mockito.mock(HttpException::class.java)
        Mockito.`when`(httpExceptionMock.code()).thenReturn(BaseViewModel.HTTP_ERROR_CODE_401)
        Mockito.`when`(utils.isNetworkAvailable()).thenReturn(true)
        Mockito.`when`(dataSource.loadRootCategoryTree()).thenReturn(Single.error(httpExceptionMock))

        viewModel.getCategories().observeForever(observer)

        Mockito.verify(observer, Mockito.never()).onChanged(arrayListOf(childCategoryTreeNode))
        Assert.assertEquals(true, viewModel.authErrorLiveData.value)
        Assert.assertNull(viewModel.getCategories().value)
    }

    @Test
    fun observeCategories_ServerError() {
        val httpExceptionMock = Mockito.mock(HttpException::class.java)
        Mockito.`when`(httpExceptionMock.code()).thenReturn(BaseViewModel.HTTP_SERVER_ERROR_500)
        Mockito.`when`(utils.isNetworkAvailable()).thenReturn(true)
        Mockito.`when`(dataSource.loadRootCategoryTree()).thenReturn(Single.error(httpExceptionMock))

        viewModel.getCategories().observeForever(observer)

        Mockito.verify(observer, Mockito.never()).onChanged(arrayListOf(childCategoryTreeNode))
        Assert.assertEquals(true, viewModel.technicalErrorLiveData.value)
        Assert.assertNull(viewModel.getCategories().value)
    }

    @Test
    fun observeCategories_TimeoutError() {
        Mockito.`when`(utils.isNetworkAvailable()).thenReturn(true)
        Mockito.`when`(dataSource.loadRootCategoryTree()).thenReturn(Single.error(SocketTimeoutException()))

        viewModel.getCategories().observeForever(observer)

        Mockito.verify(observer, Mockito.never()).onChanged(arrayListOf(childCategoryTreeNode))
        Assert.assertEquals(true, viewModel.networkErrorLiveData.value)
        Assert.assertNull(viewModel.getCategories().value)
    }

}