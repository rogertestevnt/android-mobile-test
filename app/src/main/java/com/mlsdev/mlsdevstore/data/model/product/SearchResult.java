package com.mlsdev.mlsdevstore.data.model.product;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {

    private int limit;
    private int offset;
    private int total;
    private String next;
    private String prev;
    private List<Product> itemSummaries;
    private Refinement refinement;

    public SearchResult() {
        itemSummaries = new ArrayList<>();
    }

    public int getLimit() {
        return limit;
    }

    public int getOffset() {
        return offset;
    }

    public int getTotal() {
        return total;
    }

    public String getNext() {
        return next;
    }

    public String getPrev() {
        return prev;
    }

    public List<Product> getItemSummaries() {
        return itemSummaries;
    }

    public Refinement getRefinement() {
        return refinement;
    }

    public void setRefinement(Refinement refinement) {
        this.refinement = refinement;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public void setPrev(String prev) {
        this.prev = prev;
    }

    public void setItemSummaries(List<Product> itemSummaries) {
        this.itemSummaries = itemSummaries;
    }
}
