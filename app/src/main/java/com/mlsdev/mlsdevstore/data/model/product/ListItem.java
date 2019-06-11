package com.mlsdev.mlsdevstore.data.model.product;

import android.os.Parcelable;

public interface ListItem {
    String getId();

    String getItemTitle();

    String getImageUrl();

    Price getItemPrice();

    @Condition
    String getItemCondition();

    Parcelable getParcelable();
}
