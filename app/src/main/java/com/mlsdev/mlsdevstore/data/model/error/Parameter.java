package com.mlsdev.mlsdevstore.data.model.error;

import com.google.gson.annotations.SerializedName;

public class Parameter {
    @SerializedName("name")
    private String name;
    @SerializedName("value")
    private String value;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
