package com.mlsdev.mlsdevstore.utils

import android.view.View
import android.widget.ListView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import com.mlsdev.mlsdevstore.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.AllOf
import org.hamcrest.core.StringContains
import java.text.SimpleDateFormat
import java.util.*

class CommonTestFunctions internal constructor() {

    companion object {

        fun clickButtonById(buttonId: Int) {
            Espresso.onView(ViewMatchers.withId(buttonId))
                    .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
                    .perform(ViewActions.click())
        }

        fun typeText(fieldId: Int, textToType: String) {
            Espresso.onView(ViewMatchers.withId(fieldId))
                    .perform(ViewActions.typeText(textToType), ViewActions.pressImeActionButton())
        }

        fun typeText(fieldText: String, textToType: String) {
            Espresso.onView(ViewMatchers.withText(fieldText))
                    .perform(ViewActions.typeText(textToType), ViewActions.pressImeActionButton())
        }

        fun assertElementIsDisplayed(elementId: Int) {
            Espresso.onView(ViewMatchers.withId(elementId))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }

        fun assertElementIsDisplayed(text: String) {
            Espresso.onView(ViewMatchers.withText(text))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }
        fun assertElementIsNotDisplayed(text: String) {
            Espresso.onView(ViewMatchers.withText(text))
                    .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))
        }

        fun assertElementIsNotDisplayed(elementId: Int) {
            Espresso.onView(ViewMatchers.withId(elementId))
                    .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isDisplayed())))
        }

        fun assertElementIsEnabled(text: String) {
            Espresso.onView(ViewMatchers.withText(text))
                    .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
        }

        fun assertElementIsEnabled(elementId: Int) {
            Espresso.onView(ViewMatchers.withId(elementId))
                    .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
        }

        fun clickElement(text: String) {
            Espresso.onView(ViewMatchers.withText(text))
                    .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
                    .perform(ViewActions.click())
        }

        fun clickElement(elementId: Int) {
            Espresso.onView(ViewMatchers.withId(elementId))
                    .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
                    .perform(ViewActions.click())
        }

        fun pressIMEButton() {
            ViewActions.pressImeActionButton()
        }

        fun closeSoftKeyboard() {
            Espresso.closeSoftKeyboard()
        }

        fun clearText(elementId: Int) {
            this.clickElement(elementId)
            Espresso.onView(ViewMatchers.withId(elementId))
                    .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
                    .perform(ViewActions.clearText())
            this.closeSoftKeyboard()
        }

        fun replaceText(elementId: Int, newText: String) {
            Espresso.onView(ViewMatchers.withId(elementId))
                    .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
                    .perform(ViewActions.replaceText(newText))
            this.closeSoftKeyboard()
        }

        fun clickElementonView(elementId: Int) {
            Espresso.onView(AllOf.allOf(ViewMatchers.withId(elementId), ViewMatchers.isCompletelyDisplayed()))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                    .perform(ViewActions.click())
        }

        fun clickElementonView(text: String) {
            Espresso.onView(AllOf.allOf(ViewMatchers.withText(text), ViewMatchers.isCompletelyDisplayed()))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
                    .perform(ViewActions.click())
        }

        fun swipeRight(elementId: Int) {
            Espresso.onView(ViewMatchers.withId(elementId))
                    .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
                    .perform(ViewActions.swipeRight())
        }

        fun swipeLeft(elementId: Int) {
            Espresso.onView(ViewMatchers.withId(elementId))
                    .check(ViewAssertions.matches(ViewMatchers.isEnabled()))
                    .perform(ViewActions.swipeLeft())
        }

        fun assertElementIsNotEnabled(elementId: Int) {
            Espresso.onView(ViewMatchers.withId(elementId))
                    .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())))
        }

        fun assertElementIsNotEnabled(text: String) {
            Espresso.onView(ViewMatchers.withText(text))
                    .check(ViewAssertions.matches(Matchers.not(ViewMatchers.isEnabled())))
        }

        fun assertElementMatchesText(elementId: Int, elementText: String) {
            Espresso.onView(ViewMatchers.withId(elementId))
                    .check(ViewAssertions.matches(ViewMatchers.withText(elementText)))
        }

        fun isElementDisplayed(elementId: Int): Boolean {
            try {
                Espresso.onView(ViewMatchers.withId(elementId))
                        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

            } catch (e: NoMatchingViewException) {
                return false
            }
            return true
        }

        fun isElementDisplayed(text:String): Boolean {
            try {
                Espresso.onView(ViewMatchers.withText(text))
                        .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

            } catch (e: NoMatchingViewException) {
                return false
            }
            return true
        }

        fun scrollToElement(elementId: Int) {
            Espresso.onView(ViewMatchers.withId(elementId))
                    .perform(ViewActions.scrollTo())
        }

        fun scrollToElement(text: String) {
            Espresso.onView(ViewMatchers.withText(text))
                    .perform(ViewActions.scrollTo())
        }

        fun pressBack() {
            Espresso.pressBack()
        }

        fun getText(matcher: Matcher<View>): String {
            var stringHolder = ""
            onView(matcher).perform(object : ViewAction {
                override fun getDescription(): String {
                    return ""
                }

                override fun getConstraints(): Matcher<View> {
                    return isAssignableFrom(TextView::class.java)
                }

                override fun perform(uiController: UiController, view: View) {
                    val tv = view as TextView
                    stringHolder = tv.text.toString()
                }
            })
            return stringHolder
        }

        fun listSize(size: Int): Matcher<View> {
            return object : TypeSafeMatcher<View>() {
                override fun matchesSafely(view: View): Boolean {
                    return (view as ListView).count == size
                }

                override fun describeTo(description: Description) {
                    description.appendText("ListView should have $size items")
                }
            }
        }

        fun getFileName(testName: String): String {
            val date = SimpleDateFormat("yyyyMMddHHmmss").format(Date())
            return testName + "_" + date
        }

        fun assertElementIsDisplayedOnView(elementId: Int) {
            Espresso.onView(AllOf.allOf(ViewMatchers.withId(elementId), ViewMatchers.isCompletelyDisplayed()))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }

        fun assertElementIsDisplayedOnView(text: String) {
            Espresso.onView(AllOf.allOf(ViewMatchers.withText(text), ViewMatchers.isCompletelyDisplayed()))
                    .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        }

        fun assertSubstringIsDisplayed(elementId: Int, substring: String) {
            Espresso.onView(ViewMatchers.withId(elementId)).check(ViewAssertions
                    .matches(ViewMatchers.withText(StringContains
                            .containsString(substring))))
        }

        fun clickOnRecyclerViewItemByText(elementId: Int, text: String) {
            Espresso.onView(ViewMatchers.withId(elementId))
                    .perform(RecyclerViewActions.actionOnItem<RecyclerView.ViewHolder>(
                            ViewMatchers.hasDescendant(ViewMatchers.withText(text)), ViewActions.click()))
        }

        fun clickOnRecyclerViewItemByPosition(elementId: Int, position: Int){
            Espresso.onView(ViewMatchers.withId(elementId))
                    .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click()))
        }

        fun scrollRecyclerViewByPositionAndPerformClick(recyclerViewId: Int, position: Int){
            onView(ViewMatchers.withId(recyclerViewId))
                    .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click()
                    ))
        }

    }


}
