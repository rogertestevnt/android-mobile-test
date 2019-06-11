package com.mlsdev.mlsdevstore.data.cart


import androidx.lifecycle.MutableLiveData
import com.mlsdev.mlsdevstore.data.model.order.LineItemInput
import com.mlsdev.mlsdevstore.data.model.product.Product
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class Cart @Inject
constructor() {
    val items = ArrayList<Product>()
    private val itemCountChangeListeners: MutableList<OnItemCountChangeListener>
    private val itemRemovedListeners: MutableList<OnItemRemovedListener>
    private val maxItemsReachedListeners: MutableList<OnMaxItemsReachedListener>
    private val itemAddedListeners: MutableList<OnItemAddedListener>
    val products = MutableLiveData<Product>()

    fun getTotalSum(): Double {
        var totalSum = 0.0

        for (item in items)
            totalSum += item.itemPrice.value

        return totalSum
    }

    init {
        itemCountChangeListeners = ArrayList()
        itemRemovedListeners = ArrayList()
        itemAddedListeners = ArrayList()
        maxItemsReachedListeners = ArrayList()
    }

    fun addItem(item: Product?) {
        if (item == null) {
            return
        } else if (items.size == MAX_ITEMS_PER_CHECKOUT) {
            notifyOnMaxItemsReached()
            return
        }

        for (itemFromCart in items)
            if (itemFromCart.id == item.id)
                return

        items.add(item)
        notifyItemCountChanged()
        notifyOnItemAdded(item)
    }

    fun removeItem(itemId: String?) {
        if (itemId == null || itemId.isEmpty())
            return

        for (item in items)
            if (item.id == itemId) {
                items.remove(item)
                break
            }

        notifyItemRemoved(itemId)
        notifyItemCountChanged()
    }

    private fun notifyOnMaxItemsReached() {
        for (listener in maxItemsReachedListeners)
            listener.onMaxItemsReached()
    }

    private fun notifyOnItemAdded(item: Product) {
        for (listener in itemAddedListeners)
            listener.onItemAdded(item)
    }

    private fun notifyItemRemoved(itemId: String) {
        for (listener in itemRemovedListeners)
            listener.onItemRemoved(itemId)
    }

    private fun notifyItemCountChanged() {
        for (listener in itemCountChangeListeners)
            listener.onItemCountChanged(items.size)
    }

    fun addOnItemCountChangeListener(listener: OnItemCountChangeListener) {
        itemCountChangeListeners.add(listener)
        listener.onItemCountChanged(items.size)
    }

    fun removeOnItemCountChangeListener(listener: OnItemCountChangeListener) {
        itemCountChangeListeners.remove(listener)
    }

    fun addOnItemRemovedListener(listener: OnItemRemovedListener) {
        itemRemovedListeners.add(listener)
    }

    fun removeOnItemRemovedListener(listener: OnItemRemovedListener) {
        itemRemovedListeners.remove(listener)
    }

    fun addOnMaxItemsReachedListener(listener: OnMaxItemsReachedListener) {
        maxItemsReachedListeners.add(listener)
    }

    fun removeOnMaxItemsReachedListener(listener: OnMaxItemsReachedListener) {
        maxItemsReachedListeners.remove(listener)
    }

    fun addOnItemAddedListener(listener: OnItemAddedListener) {
        itemAddedListeners.add(listener)
    }

    fun removeOnItemAddedListener(listener: OnItemAddedListener) {
        itemAddedListeners.remove(listener)
    }

    interface OnItemCountChangeListener {
        fun onItemCountChanged(count: Int)
    }

    interface OnItemRemovedListener {
        fun onItemRemoved(itemId: String)
    }

    interface OnMaxItemsReachedListener {
        fun onMaxItemsReached()
    }

    interface OnItemAddedListener {
        fun onItemAdded(item: Product)
    }

    fun getItems(): List<Product> {
        return items
    }

    fun reset() {
        items.clear()
        notifyItemCountChanged()
    }

    fun getLineItemInputs(): List<LineItemInput> {
        val lineItemInputs = ArrayList<LineItemInput>()
        items.forEach { item -> lineItemInputs.add(LineItemInput(item.id, 1)) }
        return lineItemInputs
    }

    companion object {
        const val MAX_ITEMS_PER_CHECKOUT = 4
    }
}
