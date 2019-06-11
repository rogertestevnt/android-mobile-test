package com.mlsdev.mlsdevstore.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.mlsdev.mlsdevstore.data.DataLoadState
import com.mlsdev.mlsdevstore.data.local.Key
import com.mlsdev.mlsdevstore.data.local.SharedPreferencesManager
import com.mlsdev.mlsdevstore.data.local.database.AppDatabase
import com.mlsdev.mlsdevstore.data.model.product.Product
import com.mlsdev.mlsdevstore.data.remote.datasource.BasePositionalDataSource
import com.mlsdev.mlsdevstore.data.remote.datasource.RandomProductsDataSourceFactory
import io.reactivex.Completable
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
class RandomProductsRepositoryTest {

    @Mock
    lateinit var database: AppDatabase

    @Mock
    lateinit var prefsManager: SharedPreferencesManager

    @Mock
    lateinit var dataSourceFactory: RandomProductsDataSourceFactory

    @Mock
    lateinit var dataSource: BasePositionalDataSource<Product>

    @Mock
    lateinit var dataLoadStateObserver: Observer<DataLoadState>

    lateinit var repository: RandomProductsRepository

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun beforeTest() {
        MockitoAnnotations.initMocks(this)
        repository = RandomProductsRepository(database, prefsManager, dataSourceFactory)
    }

    @Test
    fun refresh() {
        Mockito.`when`(database.deleteAllProducts()).thenReturn(Completable.complete())
        Mockito.doNothing().`when`(dataSourceFactory).invalidateDataSource()
        Mockito.doNothing().`when`(prefsManager).remove(Key.RANDOM_CATEGORY_TREE_NODE)

        repository.refresh()

        Mockito.verify(database).deleteAllProducts()
        Mockito.verify(prefsManager).remove(Key.RANDOM_CATEGORY_TREE_NODE)
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