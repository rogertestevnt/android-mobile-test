package com.mlsdev.mlsdevstore.instrumented

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.mlsdev.mlsdevstore.R
import com.mlsdev.mlsdevstore.utils.CommonTestFunctions
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)

class BasicTests:GenericTestClass() {

    @Test
    fun addAnItemToFavorites() {
        //Using a random integer to choose product for different categories at each test run
        val randomInteger = (1..5).shuffled().first()
        CommonTestFunctions.clickElement(R.id.store_flow_fragment)
        CommonTestFunctions.clickElement(R.id.button_browse_categories)
        CommonTestFunctions.clickOnRecyclerViewItemByPosition(R.id.rv_categories, randomInteger)
        Thread.sleep(3000)
        CommonTestFunctions.clickOnRecyclerViewItemByPosition(R.id.rv_products, 0)
        //Temporaly handling a http 404 error after choosing any product
        CommonTestFunctions.clickElement(context.getString(R.string.button_close))
        CommonTestFunctions.clickElement(R.id.add_to_favorites)
        //To assert that the element is displayed
        CommonTestFunctions.clickElement(R.id.favorites_flow_fragment)
        CommonTestFunctions.assertElementIsNotDisplayed(R.id.layout_empty_view)

    }

    @Test
    fun addAndRemoveAnItemFromChart() {
        //Making sure that the cart is empty
        CommonTestFunctions.clickElement(R.id.cart_flow_fragment)
        CommonTestFunctions.assertElementIsDisplayed(R.id.empty_cart_placeholder)
        CommonTestFunctions.clickElement(R.id.store_flow_fragment)
        CommonTestFunctions.clickElement(R.id.button_browse_categories)
        //Finding the category by its position in RecyclerView
        CommonTestFunctions.scrollRecyclerViewByPositionAndPerformClick(R.id.rv_categories, 13)
        CommonTestFunctions.clickOnRecyclerViewItemByPosition(R.id.rv_products, 0)
        //Temporaly handling a http 404 error after choosing any product
        CommonTestFunctions.clickElement(context.getString(R.string.button_close))
        CommonTestFunctions.clickElement(context.getString(R.string.btn_add_to_cart))
        CommonTestFunctions.clickElement(R.id.cart_flow_fragment)
        //To assert that the cart is not empty
        CommonTestFunctions.assertElementIsDisplayed(R.id.button_remove_from_cart)
        CommonTestFunctions.clickElement(R.id.button_remove_from_cart)
        CommonTestFunctions.assertElementIsDisplayed(R.id.empty_cart_placeholder)
    }

    @Test
    fun  buyOneItemFromHealthAndBeautyCategory() {
        addPersonalInfo("landon@king.com","Landon", "King")
        addShippingInfo("551233665522", "501 East St", "Palms", " Tocantins", "13080633")
        chooseOneItemAndCheckout(30)
    }

    @Test
    fun  buyOneItemFromBabyCategory() {
        addPersonalInfo("landon@king.com","Landon", "King")
        addShippingInfo("551233665522", "501 East St", "Palms", " Tocantins", "13080633")
        chooseOneItemAndCheckout(17)
    }

    private fun chooseOneItemAndCheckout(category:Int){
        CommonTestFunctions.clickElement(R.id.store_flow_fragment)
        CommonTestFunctions.clickElement(R.id.button_browse_categories)
        CommonTestFunctions.clickOnRecyclerViewItemByPosition(R.id.rv_categories, category)
        Thread.sleep(2000)
        CommonTestFunctions.clickOnRecyclerViewItemByPosition(R.id.rv_products, 0)
        //Temporaly handling a http 404 error after choosing any product
        CommonTestFunctions.clickElement(context.getString(R.string.button_close))
        CommonTestFunctions.clickElement(context.getString(R.string.btn_add_to_cart))
        CommonTestFunctions.clickElement(R.id.cart_flow_fragment)
        CommonTestFunctions.clickElement(R.id.button_checkout)
        addCardInfo("4064722257954002","0321", "207", "Landon King")
        //onView(ViewMatchers.withText(" Payment Info")).check(ViewAssertions.matches(DrawableMatchers.withDrawable(R.drawable.ic_visa)))
        CommonTestFunctions.clickElement(R.id.button_place_order)
    }

    private fun addPersonalInfo(email:String,firstName:String, lastName:String) {
        CommonTestFunctions.clickElement(R.id.account_flow_fragment)
        //Adding the personal info
        CommonTestFunctions.clickElement(R.id.button_edit_personal_info)
        CommonTestFunctions.clearText(R.id.email_edit_text)
        CommonTestFunctions.clearText(R.id.first_name_edit_text)
        CommonTestFunctions.clearText(R.id.last_name_edit_text)

        CommonTestFunctions.typeText(R.id.email_edit_text, email)
        CommonTestFunctions.typeText(R.id.first_name_edit_text, firstName)
        CommonTestFunctions.typeText(R.id.last_name_edit_text, lastName)
        CommonTestFunctions.clickElement(R.id.submit_button)
    }

    private fun addShippingInfo(phoneNumber:String, address:String, city:String, state:String, zipCode:String){
        CommonTestFunctions.clickElement(R.id.button_edit_shipping_info)
        CommonTestFunctions.clearText(R.id.edit_text_phone_number)
        CommonTestFunctions.clearText(R.id.edit_text_address)
        CommonTestFunctions.clearText(R.id.edit_text_city)
        CommonTestFunctions.clearText(R.id.edit_text_state)
        CommonTestFunctions.clearText(R.id.edit_text_postal_code)

        CommonTestFunctions.typeText(R.id.edit_text_phone_number, phoneNumber)
        CommonTestFunctions.typeText(R.id.edit_text_address,address)
        CommonTestFunctions.typeText(R.id.edit_text_city,city)
        CommonTestFunctions.typeText(R.id.edit_text_state,state)
        CommonTestFunctions.typeText(R.id.edit_text_postal_code, zipCode)
        CommonTestFunctions.clickElement(R.id.submit_button)
   }

    private fun addCardInfo(cardNumber:String, expirationDate:String, cardCvv:String, cardHolder: String) {
        CommonTestFunctions.clearText(R.id.text_card_number)
        CommonTestFunctions.clearText(R.id.text_expiration_date)
        CommonTestFunctions.clearText(R.id.text_cvv)
        CommonTestFunctions.clearText(R.id.text_card_holder)

        CommonTestFunctions.typeText(R.id.text_card_number, cardNumber)
        CommonTestFunctions.typeText(R.id.text_expiration_date, expirationDate)
        CommonTestFunctions.typeText(R.id.text_cvv, cardCvv)
        CommonTestFunctions.clearText(R.id.text_card_holder)
        CommonTestFunctions.typeText(R.id.text_card_holder, cardHolder)
    }

}