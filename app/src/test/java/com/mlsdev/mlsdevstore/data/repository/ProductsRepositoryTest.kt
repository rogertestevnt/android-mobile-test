package com.mlsdev.mlsdevstore.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.mlsdev.mlsdevstore.data.DataLoadState
import com.mlsdev.mlsdevstore.data.model.product.Product
import com.mlsdev.mlsdevstore.data.remote.datasource.BasePositionDataSourceFactory
import com.mlsdev.mlsdevstore.data.remote.datasource.BasePositionalDataSource
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
class ProductsRepositoryTest {

    @Mock
    lateinit var dataSourceFactory: BasePositionDataSourceFactory<Int, Product>

    @Mock
    lateinit var dataSource: BasePositionalDataSource<Product>

    @Mock
    lateinit var dataLoadStateObserver: Observer<DataLoadState>

    lateinit var repository: ProductsRepository

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun beforeTest() {
        MockitoAnnotations.initMocks(this)
        repository = ProductsRepository(dataSourceFactory)
    }

    @Test
    fun refresh() {
        Mockito.doNothing().`when`(dataSourceFactory).invalidateDataSource()

        repository.refresh()

        Mockito.verify(dataSourceFactory).invalidateDataSource()
    }


    @Test
    fun retry() {
        Mockito.doNothing().`when`(dataSourceFactory).retry()

        repository.retry()

        Mockito.verify(dataSourceFactory).retry()
    }

    @Test
    fun getPageLoadingState() {
        val dataLoadStateLiveData = MutableLiveData<DataLoadState>(DataLoadState.LOADING)
        val dataSourceLiveData = MutableLiveData<BasePositionalDataSource<Product>>(dataSource)
        Mockito.`when`(dataSourceFactory.getDataSourceLiveData()).thenReturn(dataSourceLiveData)
        Mockito.`when`(dataSource.getDataLoadState()).thenReturn(dataLoadStateLiveData)

        repository.getPageLoadingState().observeForever(dataLoadStateObserver)

        Mockito.verify(dataSourceFactory).getDataSourceLiveData()
        Mockito.verify(dataSource).getDataLoadState()
        Mockito.verify(dataLoadStateObserver).onChanged(DataLoadState.LOADING)
    }

}