package com.mlsdev.mlsdevstore.injections.module

import com.mlsdev.mlsdevstore.data.DataSource
import com.mlsdev.mlsdevstore.data.cart.Cart
import com.mlsdev.mlsdevstore.data.local.LocalDataSource
import com.mlsdev.mlsdevstore.data.local.SharedPreferencesManager
import com.mlsdev.mlsdevstore.data.local.database.AppDatabase
import com.mlsdev.mlsdevstore.data.model.product.Product
import com.mlsdev.mlsdevstore.data.remote.datasource.*
import com.mlsdev.mlsdevstore.data.remote.service.AuthenticationService
import com.mlsdev.mlsdevstore.data.remote.service.BrowseService
import com.mlsdev.mlsdevstore.data.remote.service.OrderService
import com.mlsdev.mlsdevstore.data.remote.service.TaxonomyService
import com.mlsdev.mlsdevstore.injections.Named
import dagger.Module
import dagger.Provides
import javax.inject.Provider
import javax.inject.Singleton

@Module
class DataSourceModule {

    @Provides
    @Singleton
    fun provideDataSource(localDataSource: LocalDataSource): DataSource {
        return localDataSource
    }

    @Provides
    @Singleton
    fun provideLocalDataSource(remoteDataSource: RemoteDataSource, appDatabase: AppDatabase): LocalDataSource {
        return LocalDataSource(remoteDataSource, appDatabase)
    }

    @Provides
    @Singleton
    fun remoteDataSource(browseService: BrowseService,
                         authenticationService: AuthenticationService,
                         taxonomyService: TaxonomyService,
                         orderService: OrderService,
                         sharedPreferencesManager: SharedPreferencesManager,
                         database: AppDatabase,
                         cart: Cart): RemoteDataSource {
        return RemoteDataSource(browseService, authenticationService, taxonomyService, orderService,
                sharedPreferencesManager, database, cart)
    }

    @Provides
    @Singleton
    fun provideProductsDataSourceFactory(provider: Provider<ProductsDataSource>): BasePositionDataSourceFactory<Int, Product> =
            ProductsDataSourceFactory(provider)

    @Provides
    @Singleton
    @Named(DataSourceFactoryType.RANDOM)
    fun provideRandomProductsDataSourceFactory(provider: Provider<RandomProductsDataSource>): BasePositionDataSourceFactory<Int, Product> =
            RandomProductsDataSourceFactory(provider)
}

class DataSourceFactoryType {
    companion object {
        const val RANDOM = "random"
    }
}
