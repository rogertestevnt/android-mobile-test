package com.mlsdev.mlsdevstore.presentation.account

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mlsdev.mlsdevstore.data.local.LocalDataSource
import com.mlsdev.mlsdevstore.data.model.user.Address
import com.mlsdev.mlsdevstore.data.model.user.PersonalInfo
import com.mlsdev.mlsdevstore.presentaion.account.AccountViewModel
import io.reactivex.Single
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AccountViewModelTest {

    @Mock
    lateinit var dataSource: LocalDataSource

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AccountViewModel
    private val personalInfo = PersonalInfo()
    private val address = Address()

    @Before
    fun beforeTest() {
        MockitoAnnotations.initMocks(this)
        personalInfo.contactFirstName = "John"
        personalInfo.contactLastName = "Smith"
        personalInfo.contactEmail = "email@email.com"

        address.phoneNumber = "+151555555"
        address.postalCode = "19101"
        address.address = "1000 Vine Street"
        address.state = "PA"
        address.city = "Philadelphia"

        viewModel = AccountViewModel(dataSource)
    }

    @Test
    fun start() {
        Mockito.`when`(dataSource.getPersonalInfo()).thenReturn(Single.just(personalInfo))
        Mockito.`when`(dataSource.getShippingInfo()).thenReturn(Single.just(address))

        viewModel.start()

        Mockito.verify(dataSource).getPersonalInfo()
        Mockito.verify(dataSource).getShippingInfo()

        Assert.assertEquals(personalInfo.contactEmail, viewModel.email.get())
        Assert.assertEquals(personalInfo.contactFirstName, viewModel.firstName.get())
        Assert.assertEquals(personalInfo.contactLastName, viewModel.lastName.get())

        Assert.assertEquals(address.phoneNumber, viewModel.phoneNumber.get())
        Assert.assertEquals(address.postalCode, viewModel.zip.get())
        Assert.assertEquals(address.address, viewModel.address.get())
        Assert.assertEquals(address.state, viewModel.state.get())
        Assert.assertEquals(address.city, viewModel.city.get())
    }

}