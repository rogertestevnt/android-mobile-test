package com.mlsdev.mlsdevstore.presentaion.account

import androidx.databinding.ObservableField
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.mlsdev.mlsdevstore.data.local.LocalDataSource
import com.mlsdev.mlsdevstore.presentaion.viewmodel.BaseViewModel
import javax.inject.Inject

class AccountViewModel @Inject
constructor(private val localDataSource: LocalDataSource) : BaseViewModel() {

    // Personal info
    val email = ObservableField<String>()
    val firstName = ObservableField<String>()
    val lastName = ObservableField<String>()

    // Shipping info
    val phoneNumber = ObservableField<String>()
    val address = ObservableField<String>()
    val city = ObservableField<String>()
    val state = ObservableField<String>()
    val zip = ObservableField<String>()

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    internal fun start() {
        compositeDisposable.add(
                localDataSource.getShippingInfo().subscribe(
                        { shippingAddress ->
                            phoneNumber.set(shippingAddress.phoneNumber)
                            address.set(shippingAddress.address)
                            city.set(shippingAddress.city)
                            state.set(shippingAddress.state)
                            zip.set(shippingAddress.postalCode)
                        },
                        { handleError(it) })
        )

        compositeDisposable.add(
                localDataSource.getPersonalInfo().subscribe(
                        { personalInfo ->
                            email.set(personalInfo.contactEmail)
                            firstName.set(personalInfo.contactFirstName)
                            lastName.set(personalInfo.contactLastName)
                        },
                        { handleError(it) })
        )
    }
}
