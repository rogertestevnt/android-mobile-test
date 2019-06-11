package com.mlsdev.mlsdevstore.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mlsdev.mlsdevstore.data.local.database.converter.CategoryConverter
import com.mlsdev.mlsdevstore.data.local.database.dao.*
import com.mlsdev.mlsdevstore.data.model.category.CategoryTree
import com.mlsdev.mlsdevstore.data.model.category.CategoryTreeNode
import com.mlsdev.mlsdevstore.data.model.image.CategoryImageEntity
import com.mlsdev.mlsdevstore.data.model.order.GuestCheckoutSession
import com.mlsdev.mlsdevstore.data.model.product.FavoriteProduct
import com.mlsdev.mlsdevstore.data.model.product.Product
import com.mlsdev.mlsdevstore.data.model.user.Address
import com.mlsdev.mlsdevstore.data.model.user.PersonalInfo
import io.reactivex.Completable
import io.reactivex.schedulers.Schedulers

@Database(entities = [
    CategoryTreeNode::class,
    CategoryTree::class,
    Address::class,
    PersonalInfo::class,
    GuestCheckoutSession::class,
    CategoryImageEntity::class,
    Product::class,
    FavoriteProduct::class
], version = 2, exportSchema = false)
@TypeConverters(CategoryConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoriesDao(): CategoriesDao
    abstract fun addressDao(): AddressDao
    abstract fun personalInfoDao(): PersonalInfoDao
    abstract fun checkoutSessionDao(): CheckoutSessionDao
    abstract fun categoryImagesDao(): CategoryImagesDao
    abstract fun productsDao(): ProductsDao
    abstract fun favoritesDao(): FavoritesDao

    open fun deleteAllProducts(): Completable = Completable.fromAction {
        productsDao().deleteAllProducts()
    }.subscribeOn(Schedulers.io())

    companion object {
        const val NAME = "local_db"
    }
}
