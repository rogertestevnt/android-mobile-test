package com.mlsdev.mlsdevstore.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.mlsdev.mlsdevstore.data.DataSource
import com.mlsdev.mlsdevstore.data.applyDefaultSchedulers
import com.mlsdev.mlsdevstore.data.local.database.AppDatabase
import com.mlsdev.mlsdevstore.data.model.category.CategoryTree
import com.mlsdev.mlsdevstore.data.model.category.CategoryTreeNode
import com.mlsdev.mlsdevstore.data.model.product.FavoriteProduct
import com.mlsdev.mlsdevstore.data.model.product.Product
import com.mlsdev.mlsdevstore.data.model.product.SearchResult
import com.mlsdev.mlsdevstore.data.model.user.Address
import com.mlsdev.mlsdevstore.data.model.user.PersonalInfo
import com.mlsdev.mlsdevstore.data.remote.datasource.RemoteDataSource
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

open class LocalDataSource(
        private val remoteDataSource: RemoteDataSource,
        private val database: AppDatabase
) : DataSource {
    override fun getFavoriteProducts(): LiveData<List<Product>> =
            Transformations.switchMap(database.favoritesDao().getFavorites()) { favorites ->
                MutableLiveData(favorites.map { it.product })
            }

    override suspend fun addToFavorites(product: Product) {
        val favoredProduct = FavoriteProduct(product)
        database.favoritesDao().insert(favoredProduct)
    }

    override suspend fun removeFromFavorites(product: Product) {
        database.favoritesDao().delete(product.itemId)
    }

    override suspend fun isProductFavored(productId: String): Boolean {
        return database.favoritesDao().checkIfExists(productId) > 0
    }

    open fun getPersonalInfo(): Single<PersonalInfo> = database.personalInfoDao().queryPersonalInfo()
            .applyDefaultSchedulers()
            .map { personalInfoList -> if (!personalInfoList.isEmpty()) personalInfoList[0] else PersonalInfo() }

    open fun getShippingInfo(): Single<Address> = database.addressDao().queryByType(Address.Type.SHIPPING)
            .applyDefaultSchedulers()
            .map { addresses -> if (!addresses.isEmpty()) addresses[0] else Address() }

    override fun loadDefaultCategoryTreeId(): Single<String> {
        val single = database.categoriesDao().queryDefaultCategoryTree()
                .flatMap { list ->
                    if (!list.isEmpty())
                        Single.just(list[0].categoryTreeId)
                    else
                        remoteDataSource.loadDefaultCategoryTreeId()
                }
        return single.applyDefaultSchedulers()
    }

    override fun loadRootCategoryTree(): Single<CategoryTree> {
        val listSingle = database.categoriesDao().queryCategoryTreeNode()
        return listSingle
                .applyDefaultSchedulers()
                .flatMap { nodes ->
                    val categoryTree = CategoryTree()
                    val categoryTreeNode = CategoryTreeNode()
                    categoryTree.categoryTreeNode = categoryTreeNode
                    categoryTreeNode.childCategoryTreeNodes = nodes

                    if (!nodes.isEmpty())
                        Single.just(categoryTree)
                    else
                        remoteDataSource.loadRootCategoryTree()
                }
    }

    override fun refreshRootCategoryTree(): Single<CategoryTree> {

        return Single.fromCallable {
            database.categoriesDao().deleteAllCategoryTreeNodes()
            database.categoriesDao().deleteAllCategoryTrees()
            1
        }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { remoteDataSource.refreshRootCategoryTree() }
    }

    override fun searchItemsByCategoryId(queries: Map<String, String>): Single<SearchResult> {
        return remoteDataSource.searchItemsByCategoryId(queries)
    }

    override fun searchMoreItemsByRandomCategory(): Single<SearchResult> {
        return remoteDataSource.searchMoreItemsByRandomCategory()
    }

    override fun getItem(itemId: String): Single<Product> {
        return remoteDataSource.getItem(itemId)
    }

    override fun resetSearchResults() {
        remoteDataSource.resetSearchResults()
    }

    open fun updatePersonalInfo(email: String?, firstName: String?, lastName: String?): Single<PersonalInfo> {
        val personalInfo = PersonalInfo()
        personalInfo.contactEmail = email
        personalInfo.contactFirstName = firstName
        personalInfo.contactLastName = lastName

        return Single.fromCallable {
            val personalInfoList = database.personalInfoDao().queryPersonalInfoSync()

            if (!personalInfoList.isEmpty())
                personalInfo.id = personalInfoList[0].id

            database.personalInfoDao().insert(personalInfo)
            return@fromCallable database.personalInfoDao().queryPersonalInfoSync()[0]
        }.applyDefaultSchedulers()
    }

    open fun updateShippingInfo(phoneNumber: String?, address: String?, city: String?, state: String?,
                                country: String?, postalCode: String?): Single<Address> {

        return Single.fromCallable {
            val insertAddress = Address()

            database.addressDao().queryByTypeSync(Address.Type.SHIPPING).getOrNull(0)?.let {
                insertAddress.id = it.id
            }

            insertAddress.phoneNumber = phoneNumber
            insertAddress.address = address
            insertAddress.city = city
            insertAddress.state = state
            insertAddress.country = country
            insertAddress.postalCode = postalCode
            database.addressDao().insert(insertAddress)
            return@fromCallable database.addressDao().queryByTypeSync(Address.Type.SHIPPING)[0]
        }.applyDefaultSchedulers()

    }
}
