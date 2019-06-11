package com.mlsdev.mlsdevstore.data.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mlsdev.mlsdevstore.data.local.database.Table
import com.mlsdev.mlsdevstore.data.model.category.CategoryTree
import com.mlsdev.mlsdevstore.data.model.category.CategoryTreeNode
import io.reactivex.Single

@Dao
interface CategoriesDao {

    @Query("delete from ${Table.CATEGORY_TREE_NODES}")
    fun deleteAllCategoryTreeNodes(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategoryTreeNode(vararg categoryTreeNodes: CategoryTreeNode)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategoryTreeNode(categoryTreeNodes: List<CategoryTreeNode>)

    @Query("select * from ${Table.CATEGORY_TREE_NODES}")
    fun queryCategoryTreeNode(): Single<List<CategoryTreeNode>>

    @Query("select * from ${Table.CATEGORY_TREE_NODES}")
    fun queryCategoryTreeNodeSync(): List<CategoryTreeNode>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategoryTree(categoryTree: CategoryTree)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCategoryTree(vararg categoryTree: CategoryTree)

    @Query("select * from ${Table.CATEGORY_TREES} limit 1")
    fun queryDefaultCategoryTree(): Single<List<CategoryTree>>

    @Query("delete from ${Table.CATEGORY_TREES}")
    fun deleteAllCategoryTrees(): Int

}
