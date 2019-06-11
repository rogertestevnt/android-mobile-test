package com.mlsdev.mlsdevstore.injections.module

import com.mlsdev.mlsdevstore.presentaion.account.AccountFragment
import com.mlsdev.mlsdevstore.presentaion.account.EditPersonalInfoFragment
import com.mlsdev.mlsdevstore.presentaion.account.EditShippingInfoFragment
import com.mlsdev.mlsdevstore.presentaion.cart.CartFragment
import com.mlsdev.mlsdevstore.presentaion.categories.CategoriesFragment
import com.mlsdev.mlsdevstore.presentaion.checkout.CheckoutFragment
import com.mlsdev.mlsdevstore.presentaion.favorites.FavoritesFragment
import com.mlsdev.mlsdevstore.presentaion.product.ProductFragment
import com.mlsdev.mlsdevstore.presentaion.products.ProductsFragment
import com.mlsdev.mlsdevstore.presentaion.store.StoreFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface FragmentBuilderModule {

    @ContributesAndroidInjector
    fun contributeStoreFragment(): StoreFragment

    @ContributesAndroidInjector
    fun contributeCartFragment(): CartFragment

    @ContributesAndroidInjector
    fun contributeAccountFragment(): AccountFragment

    @ContributesAndroidInjector
    fun contributeCategoriesFragment(): CategoriesFragment

    @ContributesAndroidInjector
    fun contributeProductsFragment(): ProductsFragment

    @ContributesAndroidInjector
    fun contributeProducFragment(): ProductFragment

    @ContributesAndroidInjector
    fun contributeEditPersonalInfoFragment(): EditPersonalInfoFragment

    @ContributesAndroidInjector
    fun contributeEditShippingInfoFragment(): EditShippingInfoFragment

    @ContributesAndroidInjector
    fun contributeCheckoutFragment(): CheckoutFragment

    @ContributesAndroidInjector
    fun contributeFavoritesFragment(): FavoritesFragment
}
