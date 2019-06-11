package com.mlsdev.mlsdevstore.data.model.category;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mlsdev.mlsdevstore.data.local.database.Table;

import java.util.List;

@Entity(tableName = Table.CATEGORY_TREES)
public class CategoryTree {

    @SerializedName("categoryTreeId")
    @PrimaryKey
    @NonNull
    private String categoryTreeId;

    @SerializedName("categoryTreeVersion")
    @Ignore
    private String categoryTreeVersion;

    @SerializedName(value = "rootCategoryNode", alternate = {"categorySubtreeNode"})
    @Expose
    @Ignore
    private CategoryTreeNode categoryTreeNode;

    @SerializedName("applicableMarketplaceIds")
    @Expose
    @Ignore
    private List<String> applicableMarketplaceIds;

    public void setCategoryTreeId(String categoryTreeId) {
        this.categoryTreeId = categoryTreeId;
    }

    public void setCategoryTreeNode(CategoryTreeNode categoryTreeNode) {
        this.categoryTreeNode = categoryTreeNode;
    }

    public String getCategoryTreeId() {
        return categoryTreeId;
    }

    public String getCategoryTreeVersion() {
        return categoryTreeVersion;
    }

    public CategoryTreeNode getCategoryTreeNode() {
        return categoryTreeNode;
    }

    public List<String> getApplicableMarketplaceIds() {
        return applicableMarketplaceIds;
    }
}
