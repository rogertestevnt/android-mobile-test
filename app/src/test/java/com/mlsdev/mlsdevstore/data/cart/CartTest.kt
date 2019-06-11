package com.mlsdev.mlsdevstore.data.cart

import com.mlsdev.mlsdevstore.data.model.product.Condition
import com.mlsdev.mlsdevstore.data.model.product.Price
import com.mlsdev.mlsdevstore.data.model.product.Product
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.MockitoAnnotations
import java.util.*

class CartTest {

    private val cart = Cart()
    lateinit var item: Product

    @Before
    fun beforeTest() {
        MockitoAnnotations.initMocks(this)
        item = createItem()
    }

    @Test
    fun addItem() {
        cart.addItem(item)
        Assert.assertEquals(1, cart.items.size)
        Assert.assertEquals(item.id, cart.items[0].id)
        Assert.assertEquals(item.itemTitle, cart.items[0].title)
        Assert.assertEquals(item.itemCondition, cart.items[0].condition)
        Assert.assertEquals(item.itemPrice.currency, Objects.requireNonNull<Price>(cart.items[0].price).currency)
        Assert.assertEquals(item.itemPrice.value, Objects.requireNonNull<Price>(cart.items[0].price).value, 0.0)
    }

    @Test
    fun addItem_MaxItemCountWasReached() {
        cart.addItem(createItem())
        cart.addItem(createItem())
        cart.addItem(createItem())
        cart.addItem(createItem())
        cart.addItem(createItem())

        Assert.assertEquals(4, cart.items.size)
    }

    @Test
    fun removeItem() {
        cart.addItem(item)
        Assert.assertFalse(cart.items.isEmpty())

        cart.removeItem(item.id)
        Assert.assertTrue(cart.items.isEmpty())
    }

    private fun createItem(): Product {
        val price = Price()
        price.currency = "USD"
        price.value = Math.random() * 1000

        val item = Product()
        item.itemId = price.value.hashCode().toString()
        item.title = "title"
        item.condition = Condition.New
        item.price = price

        return item
    }
}
