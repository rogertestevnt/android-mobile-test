package com.mlsdev.mlsdevstore.data.model.category;

import com.mlsdev.mlsdevstore.data.local.database.Column;
import com.mlsdev.mlsdevstore.data.local.database.Table;

import java.util.ArrayList;
import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = Table.CATEGORY_TREE_NODES,
        indices = {@Index(Column.CATEGORY_TREE_NODE_ID)})
public class CategoryTreeNode {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = Column.CATEGORY_TREE_NODE_ID)
    private long id;

    @Embedded
    private Category category;

    @ColumnInfo(name = Column.PARENT_CATEGORY_TREE_NODE_HREF)
    private String parentCategoryTreeNodeHref;

    @Ignore
    private List<CategoryTreeNode> childCategoryTreeNodes;

    @ColumnInfo(name = Column.CATEGORY_TREE_NODE_LEVEL)
    private int categoryTreeNodeLevel;

    @ColumnInfo(name = Column.LEAF_CATEGORY_TREE_NODE)
    private boolean leafCategoryTreeNode;

    //region getters

    public long getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public String getParentCategoryTreeNodeHref() {
        return parentCategoryTreeNodeHref;
    }

    public List<CategoryTreeNode> getChildCategoryTreeNodes() {
        return childCategoryTreeNodes != null ? childCategoryTreeNodes : new ArrayList<>();
    }

    public int getCategoryTreeNodeLevel() {
        return categoryTreeNodeLevel;
    }

    public boolean isLeafCategoryTreeNode() {
        return leafCategoryTreeNode;
    }

    //endregion

    //region setters

    public void setId(long id) {
        this.id = id;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setParentCategoryTreeNodeHref(String parentCategoryTreeNodeHref) {
        this.parentCategoryTreeNodeHref = parentCategoryTreeNodeHref;
    }

    public void setChildCategoryTreeNodes(List<CategoryTreeNode> childCategoryTreeNodes) {
        this.childCategoryTreeNodes = childCategoryTreeNodes;
    }

    public void setCategoryTreeNodeLevel(int categoryTreeNodeLevel) {
        this.categoryTreeNodeLevel = categoryTreeNodeLevel;
    }

    public void setLeafCategoryTreeNode(boolean leafCategoryTreeNode) {
        this.leafCategoryTreeNode = leafCategoryTreeNode;
    }

    //endregion
}
