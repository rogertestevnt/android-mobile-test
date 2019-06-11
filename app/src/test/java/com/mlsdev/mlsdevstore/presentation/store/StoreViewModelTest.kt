package com.mlsdev.mlsdevstore.presentation.store

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.RxPagedListBuilder
import com.mlsdev.mlsdevstore.data.DataLoadState
import com.mlsdev.mlsdevstore.data.remote.datasource.getPagingConfig
import com.mlsdev.mlsdevstore.data.repository.RandomProductsRepository
import com.mlsdev.mlsdevstore.presentaion.store.StoreViewModel
import com.mlsdev.mlsdevstore.presentaion.utils.Utils
import com.mlsdev.mlsdevstore.stub.data.remote.datasource.ProductsDataSourceFactoryStub
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
class StoreViewModelTest {

    @Mock
    lateinit var utils: Utils

    @Mock
    lateinit var repository: RandomProductsRepository

    @Mock
    lateinit var loadingStateObserver: Observer<DataLoadState>

    lateinit var viewModel: StoreViewModel

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun beforeTest() {
        MockitoAnnotations.initMocks(this)
        viewModel = Mockito.spy(StoreViewModel(utils, repository))
    }

    @Test
    fun observeLoadingState() {
        Mockito.`when`(repository.getPageLoadingState()).thenReturn(MutableLiveData<DataLoadState>(DataLoadState.LOADING))

        viewModel.loadingState.observeForever(loadingStateObserver)

        Mockito.verify(loadingStateObserver).onChanged(DataLoadState.LOADING)
        Assert.assertEquals(DataLoadState.LOADING, viewModel.loadingState.value)
    }

    @Test
    fun observePagedList() {
        val observablePagedList = RxPagedListBuilder(ProductsDataSourceFactoryStub(), getPagingConfig()).buildObservable()
        Mockito.`when`(repository.getItems()).thenReturn(observablePagedList)

        viewModel.products.test()
                .assertSubscribed()
                .assertValueAt(0, observablePagedList.blockingFirst())
                .assertNoErrors()
                .dispose()
    }

    @Test
    fun refresh() {
        Mockito.doNothing().`when`(repository).refresh()
        Mockito.`when`(utils.isNetworkAvailable()).thenReturn(true)

        viewModel.refresh()

        Mockito.verify(repository).refresh()
    }

    @Test
    fun retry() {
        Mockito.doNothing().`when`(repository).retry()
        Mockito.`when`(utils.isNetworkAvailable()).thenReturn(true)

        viewModel.retry()

        Mockito.verify(repository).retry()
    }
}