package com.mlsdev.mlsdevstore.data.remote.datasource

import android.util.ArrayMap
import android.util.Base64
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mlsdev.mlsdevstore.BuildConfig
import com.mlsdev.mlsdevstore.data.DataSource
import com.mlsdev.mlsdevstore.data.applyDefaultSchedulers
import com.mlsdev.mlsdevstore.data.cart.Cart
import com.mlsdev.mlsdevstore.data.local.Key
import com.mlsdev.mlsdevstore.data.local.SharedPreferencesManager
import com.mlsdev.mlsdevstore.data.local.database.AppDatabase
import com.mlsdev.mlsdevstore.data.model.authentication.AppAccessToken
import com.mlsdev.mlsdevstore.data.model.authentication.AppAccessTokenRequestBody
import com.mlsdev.mlsdevstore.data.model.category.CategoryTree
import com.mlsdev.mlsdevstore.data.model.category.CategoryTreeNode
import com.mlsdev.mlsdevstore.data.model.order.GuestCheckoutSession
import com.mlsdev.mlsdevstore.data.model.order.GuestCheckoutSessionRequest
import com.mlsdev.mlsdevstore.data.model.order.PostOrderResult
import com.mlsdev.mlsdevstore.data.model.product.CategoryDistribution
import com.mlsdev.mlsdevstore.data.model.product.Product
import com.mlsdev.mlsdevstore.data.model.product.Refinement
import com.mlsdev.mlsdevstore.data.model.product.SearchResult
import com.mlsdev.mlsdevstore.data.remote.service.AuthenticationService
import com.mlsdev.mlsdevstore.data.remote.service.BrowseService
import com.mlsdev.mlsdevstore.data.remote.service.OrderService
import com.mlsdev.mlsdevstore.data.remote.service.TaxonomyService
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.io.UnsupportedEncodingException
import java.util.*

class RemoteDataSource(private val browseService: BrowseService,
                       private val authenticationService: AuthenticationService,
                       private val taxonomyService: TaxonomyService,
                       private val orderService: OrderService,
                       private val sharedPreferencesManager: SharedPreferencesManager,
                       private val database: AppDatabase,
                       private val cart: Cart) : DataSource {
    override fun getFavoriteProducts(): LiveData<List<Product>> = MutableLiveData()

    override suspend fun addToFavorites(product: Product) {
    }

    override suspend fun removeFromFavorites(product: Product) {
    }

    override suspend fun isProductFavored(productId: String): Boolean = false

    private var searchOffset = 0
    private var searchLimit = 10
    private val searchItems = ArrayList<Product>()

    val appAccessToken: Single<AppAccessToken>
        get() {
            val originalOAuthCredentials = BuildConfig.CLIENT_ID + ":" + BuildConfig.CLIENT_SECRET
            var oAuthCredentialsBytes = ByteArray(0)
            try {
                oAuthCredentialsBytes = originalOAuthCredentials.toByteArray(charset("UTF-8"))
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }

            val encodedOAuthCredentials = Base64.encodeToString(oAuthCredentialsBytes, Base64.NO_WRAP)
            val headers = HashMap<String, String>(2)
            headers["Content-Type"] = "application/x-www-form-urlencoded"
            headers["Authorization"] = "Basic $encodedOAuthCredentials"
            val body = AppAccessTokenRequestBody()

            return authenticationService.getAppAccessToken(headers, body.fields)
                    .applyDefaultSchedulers()
                    .map { appAccessToken ->
                        val currentTime = Calendar.getInstance().timeInMillis
                        val expirationDate = currentTime + appAccessToken.expiresIn!!
                        appAccessToken.expirationDate = expirationDate
                        appAccessToken
                    }
                    .doOnSuccess { appAccessToken -> sharedPreferencesManager.save(Key.APPLICATION_ACCESS_TOKEN, appAccessToken) }
        }

    private val searchResultConsumer = { searchResult: SearchResult ->
        searchItems.addAll(searchResult.itemSummaries)
        searchLimit = searchResult.limit
        searchOffset = searchItems.size
        searchResult.refinement = categoryRefinement
    }

    private val categoryRefinement: Refinement
        get() {
            val node = sharedPreferencesManager[Key.RANDOM_CATEGORY_TREE_NODE, CategoryTreeNode::class.java]
            val refinement = Refinement()
            val distributionList = ArrayList<CategoryDistribution>(1)
            val distribution = CategoryDistribution()
            distribution.categoryId = node!!.category.categoryId
            distribution.categoryName = node.category.categoryName
            distributionList.add(distribution)
            refinement.categoryDistributions = distributionList
            return refinement
        }

    override fun loadDefaultCategoryTreeId(): Single<String> {
        return taxonomyService.defaultCategoryTreeId
                .applyDefaultSchedulers()
                .map { it.categoryTreeId }
                .doOnSuccess { this.saveDefaultCategoryTreeId(it) }
    }

    override fun loadRootCategoryTree(): Single<CategoryTree> {
        return loadDefaultCategoryTreeId()
                .flatMap { defaultCategoryTreeId ->
                    taxonomyService.getCategoryTree(defaultCategoryTreeId).applyDefaultSchedulers()
                }
                .doOnSuccess { this.saveCategoryTreeNodes(it) }
    }

    override fun refreshRootCategoryTree(): Single<CategoryTree> {
        return loadRootCategoryTree()
    }

    override fun searchItemsByCategoryId(queries: Map<String, String>): Single<SearchResult> {
        return browseService.searchItemsByCategoryId(queries)
                .applyDefaultSchedulers()
    }

    override fun searchMoreItemsByRandomCategory(): Single<SearchResult> {
        return browseService.searchItemsByCategoryId(prepareSearchQueryMap())
                .applyDefaultSchedulers()
                .doOnSuccess(searchResultConsumer)
    }

    override fun getItem(itemId: String): Single<Product> {
        return browseService.getItem(itemId).applyDefaultSchedulers()
    }

    override fun resetSearchResults() {
        searchOffset = 0
        searchItems.clear()
        sharedPreferencesManager.remove(Key.RANDOM_CATEGORY_TREE_NODE)
    }

    private fun prepareSearchQueryMap(): Map<String, String> {
        val node = sharedPreferencesManager[Key.RANDOM_CATEGORY_TREE_NODE, CategoryTreeNode::class.java]
        val queries = ArrayMap<String, String>()
        queries["category_ids"] = node!!.category.categoryId
        queries["limit"] = searchLimit.toString()
        queries["offset"] = searchItems.size.toString()
        return queries
    }

    fun initGuestCheckoutSession(guestCheckoutSessionRequest: GuestCheckoutSessionRequest): Single<GuestCheckoutSession> =
            orderService.initiateGuestCheckoutSession(guestCheckoutSessionRequest)
                    .applyDefaultSchedulers()
                    .doOnSuccess { cart.reset() }

    fun postOrder(checkoutSessionId: String): Single<PostOrderResult> =
            orderService.postOrder(checkoutSessionId).applyDefaultSchedulers()

    private fun saveCategoryTreeNodes(categoryTree: CategoryTree) {
        Completable.create { database.categoriesDao().insertCategoryTreeNode(categoryTree.categoryTreeNode.childCategoryTreeNodes) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }

    private fun saveDefaultCategoryTreeId(categoryTreeId: String) {
        val categoryTree = CategoryTree()
        categoryTree.categoryTreeId = categoryTreeId
        Completable.create { database.categoriesDao().insertCategoryTree(categoryTree) }
                .subscribeOn(Schedulers.io())
                .subscribe()
    }
}
