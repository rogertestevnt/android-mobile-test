package com.mlsdev.mlsdevstore.instrumented

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.mlsdev.mlsdevstore.R
import com.mlsdev.mlsdevstore.utils.CommonTestFunctions
import org.junit.FixMethodOrder
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

@LargeTest
@RunWith(AndroidJUnit4::class)

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class BasicTests:GenericTestClass() {

    @Test
    fun addAnItemToFavorites() {
        CommonTestFunctions.clickElement(R.id.store_flow_fragment)
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
        CommonTestFunctions.clickElement(R.id.store_flow_fragment)
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
    fun buyOneItemFromHealthAndBeautyCategory() {
        addPersonalInfo("elvis@nelson.com","Elvis", "Nelson")
        addShippingInfo("551993366552", "503 West St", "Palms", " Florida", "43080633")
        chooseOneItemAndCheckout(30,"5102316149946077","1120", "859", "Elvis Nelson")
    }

    @Test
    fun buyOneItemFromBabyCategory() {
        addPersonalInfo("landon@king.com","Landon", "King")
        addShippingInfo("551233665522", "501 East St", "Palms", " Tocantins", "13080633")
        chooseOneItemAndCheckout(17,"4064722257954002","0321", "207", "Landon King")
    }

    private fun chooseOneItemAndCheckout(category:Int,cardNumber:String, expirationDate:String, cardCvv:String, cardHolder: String) {
        CommonTestFunctions.clickElement(R.id.store_flow_fragment)
        CommonTestFunctions.clickElement(R.id.button_browse_categories)
        CommonTestFunctions.clickOnRecyclerViewItemByPosition(R.id.rv_categories, category)
        Thread.sleep(5000)
        CommonTestFunctions.clickOnRecyclerViewItemByPosition(R.id.rv_products, 0)
        //Temporaly handling a http 404 error after choosing any product
        CommonTestFunctions.clickElement(context.getString(R.string.button_close))
        CommonTestFunctions.clickElement(context.getString(R.string.btn_add_to_cart))
        CommonTestFunctions.clickElement(R.id.cart_flow_fragment)
        CommonTestFunctions.clickElement(R.id.button_checkout)
        addCardInfo(cardNumber, expirationDate, cardCvv, cardHolder)
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