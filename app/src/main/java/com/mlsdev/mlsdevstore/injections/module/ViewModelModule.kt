package com.mlsdev.mlsdevstore.injections.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

import com.mlsdev.mlsdevstore.injections.ViewModelFactory
import com.mlsdev.mlsdevstore.presentaion.account.AccountViewModel
import com.mlsdev.mlsdevstore.presentaion.account.EditAccountViewModel
import com.mlsdev.mlsdevstore.presentaion.bottom_navigation.BottomNavigationViewModel
import com.mlsdev.mlsdevstore.presentaion.cart.CartViewModel
import com.mlsdev.mlsdevstore.presentaion.categories.CategoriesViewModel
import com.mlsdev.mlsdevstore.presentaion.checkout.CheckoutViewModel
import com.mlsdev.mlsdevstore.presentaion.favorites.FavoritesViewModel
import com.mlsdev.mlsdevstore.presentaion.product.ProductDetailsViewModel
import com.mlsdev.mlsdevstore.presentaion.products.ProductsViewModel
import com.mlsdev.mlsdevstore.presentaion.splashscreen.SplashScreenViewModel
import com.mlsdev.mlsdevstore.presentaion.store.StoreViewModel

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(StoreViewModel::class)
    fun bindStoreViewModel(storeViewModel: StoreViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CartViewModel::class)
    fun bindCartViewModel(cartViewModel: CartViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AccountViewModel::class)
    fun bindAccountViewModel(accountViewModel: AccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SplashScreenViewModel::class)
    fun bindSplashScreenViewModel(splashScreenViewModel: SplashScreenViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CategoriesViewModel::class)
    fun bindCategoriesViewModel(categoriesViewModel: CategoriesViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductDetailsViewModel::class)
    fun bindProductDetailsViewModel(productDetailsViewModel: ProductDetailsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(BottomNavigationViewModel::class)
    fun bindBottomNavigationViewModel(bottomNavigationViewModel: BottomNavigationViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(CheckoutViewModel::class)
    fun bindCheckoutViewModel(checkoutViewModel: CheckoutViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EditAccountViewModel::class)
    fun bindEditAccountViewModel(editAccountViewModel: EditAccountViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ProductsViewModel::class)
    fun bindProductsViewModel(productsViewModel: ProductsViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(FavoritesViewModel::class)
    fun bindFavoritesViewModel(favoritesViewModel: FavoritesViewModel): ViewModel

    @Binds
    fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}
