package com.mlsdev.mlsdevstore.data.model.product;

import com.mlsdev.mlsdevstore.data.model.category.Category;

public class CategoryDistribution extends Category {

    private int matchCount;
    private String refinementHref;

    public int getMatchCount() {
        return matchCount;
    }

    public String getRefinementHref() {
        return refinementHref;
    }

    public void setMatchCount(int matchCount) {
        this.matchCount = matchCount;
    }

    public void setRefinementHref(String refinementHref) {
        this.refinementHref = refinementHref;
    }
}
