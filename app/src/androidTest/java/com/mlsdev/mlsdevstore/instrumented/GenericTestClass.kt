package com.mlsdev.mlsdevstore.instrumented



import android.content.Intent
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.IdlingRegistry
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import org.junit.After
import org.junit.Before
import org.junit.Rule
import com.mlsdev.mlsdevstore.MLSDevStoreApplication
import com.mlsdev.mlsdevstore.presentaion.splashscreen.SplashScreenActivity
import com.mlsdev.mlsdevstore.presentaion.utils.EspressoIdlingResource

open class GenericTestClass {

    lateinit var context: MLSDevStoreApplication

    @get:Rule
    val activityRule = ActivityTestRule(SplashScreenActivity::class.java, false,
            false)

//    @get:Rule
//    var testName = TestName()

    @Before
    open fun beforeRun() {
        context = ApplicationProvider.getApplicationContext()
        activityRule.launchActivity(Intent(context, SplashScreenActivity::class.java))
        //IdlingRegistry.getInstance().register(EspressoIdlingResource.idlingResource)
        Thread.sleep(5000)
    }

    @After
    fun afterRun() {
        //IdlingRegistry.getInstance().unregister(EspressoIdlingResource.idlingResource)
        activityRule.finishActivity()
    }


}