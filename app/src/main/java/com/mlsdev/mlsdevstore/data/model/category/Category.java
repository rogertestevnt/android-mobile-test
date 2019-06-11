package com.mlsdev.mlsdevstore.data.model.category;

import androidx.annotation.NonNull;

public class Category {

    private String categoryId = "default";

    private String categoryName;

    public String getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryId(@NonNull String categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

}