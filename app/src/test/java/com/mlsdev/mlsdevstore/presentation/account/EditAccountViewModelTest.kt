package com.mlsdev.mlsdevstore.presentation.account

import android.app.Application
import android.content.res.Configuration
import android.content.res.Resources
import android.os.LocaleList
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.mlsdev.mlsdevstore.data.local.LocalDataSource
import com.mlsdev.mlsdevstore.data.model.user.Address
import com.mlsdev.mlsdevstore.data.model.user.PersonalInfo
import com.mlsdev.mlsdevstore.presentaion.account.EditAccountViewModel
import com.mlsdev.mlsdevstore.presentaion.utils.*
import io.reactivex.Completable
import io.reactivex.Single
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import java.util.*

@RunWith(MockitoJUnitRunner::class)
class EditAccountViewModelTest {

    @Mock
    lateinit var dataSource: LocalDataSource

    @Mock
    lateinit var fieldsValidator: FieldsValidator

    @Mock
    lateinit var application: Application

    @Mock
    lateinit var resources: Resources

    @Mock
    lateinit var configuration: Configuration

    @Mock
    lateinit var localeList: LocaleList

    @Mock
    lateinit var observer: Observer<Boolean>

    private var locale = Locale("en", "US")
    private lateinit var viewModel: EditAccountViewModel
    private val personalInfo = PersonalInfo()
    private val personalInfoUpdate = PersonalInfo()
    private val address = Address()
    private val addressUpdate = Address()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Before
    fun beforeTest() {
        personalInfo.contactFirstName = "John"
        personalInfo.contactLastName = "Smith"
        personalInfo.contactEmail = "email@email.com"

        address.phoneNumber = "+151555555"
        address.postalCode = "19101"
        address.address = "1000 Vine Street"
        address.state = "PA"
        address.city = "Philadelphia"

        personalInfoUpdate.contactFirstName = "${personalInfo.contactFirstName}_"
        personalInfoUpdate.contactLastName = "${personalInfo.contactLastName}_"
        personalInfoUpdate.contactEmail = "${personalInfo.contactEmail}_"

        addressUpdate.phoneNumber = "${address.phoneNumber}_"
        addressUpdate.postalCode = "${address.postalCode}_"
        addressUpdate.address = "${address.address}_"
        addressUpdate.state = "${address.state}_"
        addressUpdate.city = "${address.city}_"

        MockitoAnnotations.initMocks(this)
        `when`(application.resources).thenReturn(resources)
        `when`(resources.configuration).thenReturn(configuration)
        `when`(configuration.locales).thenReturn(localeList)
        `when`(localeList[0]).thenReturn(locale)

        viewModel = EditAccountViewModel(application, dataSource, fieldsValidator)
    }

    @Test
    fun start() {
        `when`(dataSource.getPersonalInfo()).thenReturn(Single.just(personalInfo))
        `when`(dataSource.getShippingInfo()).thenReturn(Single.just(address))

        viewModel.start()

        verify<LocalDataSource>(dataSource).getPersonalInfo()
        verify<LocalDataSource>(dataSource).getShippingInfo()

        assertEquals(personalInfo.contactEmail, viewModel.email.get())
        assertEquals(personalInfo.contactFirstName, viewModel.firstName.get())
        assertEquals(personalInfo.contactLastName, viewModel.lastName.get())

        assertEquals(address.phoneNumber, viewModel.phoneNumber.get())
        assertEquals(address.postalCode, viewModel.postalCode.get())
        assertEquals(address.address, viewModel.address.get())
        assertEquals(address.state, viewModel.state.get())
        assertEquals(address.city, viewModel.city.get())
    }

    @Test
    fun onSubmitPersonalInfoClick() {
        `when`(fieldsValidator.putField(EMAIL, personalInfoUpdate.contactEmail)).thenReturn(fieldsValidator)
        `when`(fieldsValidator.putField(FIRST_NAME, personalInfoUpdate.contactFirstName)).thenReturn(fieldsValidator)
        `when`(fieldsValidator.putField(LAST_NAME, personalInfoUpdate.contactLastName)).thenReturn(fieldsValidator)
        `when`(fieldsValidator.validateFields()).thenReturn(Completable.complete())
        `when`(dataSource.updatePersonalInfo(personalInfoUpdate.contactEmail,
                personalInfoUpdate.contactFirstName,
                personalInfoUpdate.contactLastName)).thenReturn(Single.just(personalInfoUpdate))

        viewModel.email.set(personalInfoUpdate.contactEmail)
        viewModel.firstName.set(personalInfoUpdate.contactFirstName)
        viewModel.lastName.set(personalInfoUpdate.contactLastName)

        viewModel.profileDataUpdated.observeForever(observer)
        viewModel.onSubmitPersonalInfoClick()

        verify(observer).onChanged(true)
        verify(dataSource).updatePersonalInfo(
                personalInfoUpdate.contactEmail,
                personalInfoUpdate.contactFirstName,
                personalInfoUpdate.contactLastName)
    }

    @Test
    fun onSubmitPersonalInfoClick_InvalidFields() {
        val errorEmpty = "empty"
        val errorEmail = "incorrect"
        val incorrectEmail = "incorrect email"
        val invalidFields = hashMapOf(Pair(EMAIL, errorEmail), Pair(FIRST_NAME, errorEmpty), Pair(LAST_NAME, errorEmpty))
        `when`(fieldsValidator.putField(EMAIL, incorrectEmail)).thenReturn(fieldsValidator)
        `when`(fieldsValidator.putField(FIRST_NAME, "")).thenReturn(fieldsValidator)
        `when`(fieldsValidator.putField(LAST_NAME, "")).thenReturn(fieldsValidator)
        `when`(fieldsValidator.validateFields()).thenReturn(Completable.error(FieldsValidator.ValidationError(invalidFields)))

        viewModel.email.set(incorrectEmail)
        viewModel.firstName.set("")
        viewModel.lastName.set("")

        viewModel.profileDataUpdated.observeForever(observer)
        viewModel.onSubmitPersonalInfoClick()

        Assert.assertEquals(errorEmail, viewModel.emailError.get())
        Assert.assertEquals(errorEmpty, viewModel.firstNameError.get())
        Assert.assertEquals(errorEmpty, viewModel.lastNameError.get())
        verify(fieldsValidator).putField(EMAIL, incorrectEmail)
        verify(fieldsValidator).putField(FIRST_NAME, "")
        verify(fieldsValidator).putField(LAST_NAME, "")
        verify(observer, never()).onChanged(true)
        verify(dataSource, never()).updatePersonalInfo(incorrectEmail, "", "")
    }

    @Test
    fun onSubmitShippingInfoClick() {
        `when`(fieldsValidator.putField(FIELD_PHONE, addressUpdate.phoneNumber)).thenReturn(fieldsValidator)
        `when`(fieldsValidator.putField(FIELD_ADDRESS, addressUpdate.address)).thenReturn(fieldsValidator)
        `when`(fieldsValidator.putField(FIELD_CITY, addressUpdate.city)).thenReturn(fieldsValidator)
        `when`(fieldsValidator.putField(FIELD_STATE, addressUpdate.state)).thenReturn(fieldsValidator)
        `when`(fieldsValidator.putField(FIELD_POSTAL_CODE, addressUpdate.postalCode)).thenReturn(fieldsValidator)
        `when`(fieldsValidator.validateFields()).thenReturn(Completable.complete())
        `when`(dataSource.updateShippingInfo(
                addressUpdate.phoneNumber, addressUpdate.address, addressUpdate.city, addressUpdate.state,
                locale.country, addressUpdate.postalCode
        )).thenReturn(Single.just(addressUpdate))

        viewModel.phoneNumber.set(addressUpdate.phoneNumber)
        viewModel.postalCode.set(addressUpdate.postalCode)
        viewModel.address.set(addressUpdate.address)
        viewModel.state.set(addressUpdate.state)
        viewModel.city.set(addressUpdate.city)

        viewModel.profileDataUpdated.observeForever(observer)
        viewModel.onSubmitShippingInfoClick()

        verify(observer).onChanged(true)
        verify(dataSource).updateShippingInfo(
                addressUpdate.phoneNumber,
                addressUpdate.address,
                addressUpdate.city,
                addressUpdate.state,
                locale.country,
                addressUpdate.postalCode)
    }

    @Test
    fun onSubmitShippingInfoClick_InvalidFields() {
        val error = "empty"
        val invalidFields = hashMapOf(
                Pair(FIELD_PHONE, error),
                Pair(FIELD_ADDRESS, error),
                Pair(FIELD_CITY, error),
                Pair(FIELD_STATE, error),
                Pair(FIELD_POSTAL_CODE, error))

        `when`(fieldsValidator.putField(FIELD_POSTAL_CODE, "")).thenReturn(fieldsValidator)
        `when`(fieldsValidator.putField(FIELD_ADDRESS, "")).thenReturn(fieldsValidator)
        `when`(fieldsValidator.putField(FIELD_PHONE, "")).thenReturn(fieldsValidator)
        `when`(fieldsValidator.putField(FIELD_STATE, "")).thenReturn(fieldsValidator)
        `when`(fieldsValidator.putField(FIELD_CITY, "")).thenReturn(fieldsValidator)
        `when`(fieldsValidator.validateFields()).thenReturn(Completable.error(FieldsValidator.ValidationError(invalidFields)))

        viewModel.phoneNumber.set("")
        viewModel.postalCode.set("")
        viewModel.address.set("")
        viewModel.state.set("")
        viewModel.city.set("")

        viewModel.profileDataUpdated.observeForever(observer)
        viewModel.onSubmitShippingInfoClick()

        Assert.assertEquals(error, viewModel.phoneNumberError.get())
        Assert.assertEquals(error, viewModel.postalCodeError.get())
        Assert.assertEquals(error, viewModel.addressError.get())
        Assert.assertEquals(error, viewModel.stateError.get())
        Assert.assertEquals(error, viewModel.cityError.get())
        verify(fieldsValidator).putField(FIELD_POSTAL_CODE, "")
        verify(fieldsValidator).putField(FIELD_ADDRESS, "")
        verify(fieldsValidator).putField(FIELD_PHONE, "")
        verify(fieldsValidator).putField(FIELD_STATE, "")
        verify(fieldsValidator).putField(FIELD_CITY, "")
        verify(observer, never()).onChanged(true)
        verify(dataSource, never()).updateShippingInfo("", "", "", "", locale.country, "")
    }

}
