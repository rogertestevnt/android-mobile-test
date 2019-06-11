package com.mlsdev.mlsdevstore.data.local.database

import com.mlsdev.mlsdevstore.data.model.category.Category
import com.mlsdev.mlsdevstore.data.model.category.CategoryTreeNode

annotation class Column {
    companion object {
        /**
         * @See [Category]
         */
        const val CATEGORY_ID = "category_id"
        const val CATEGORY_NAME = "category_name"

        /**
         * @See [CategoryTreeNode]
         */
        const val CATEGORY_TREE_NODE_ID = "category_tree_node_id"
        const val CATEGORY_TREE_NODE_ID_FOREIGN_KEY = "category_tree_node_id_foreign_key"
        const val CATEGORY = "category"
        const val CATEGORY_TREE_NODE_LEVEL = "category_tree_node_level"
        const val LEAF_CATEGORY_TREE_NODE = "leaf_category_tree_node"
        const val PARENT_CATEGORY_TREE_NODE_HREF = "parent_category_tree_node_href"
    }
}
