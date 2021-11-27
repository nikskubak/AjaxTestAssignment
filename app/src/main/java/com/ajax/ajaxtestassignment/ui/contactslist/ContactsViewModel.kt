package com.ajax.ajaxtestassignment.ui.contactslist

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ajax.ajaxtestassignment.api.RetrofitServicesProvider
import com.ajax.ajaxtestassignment.base.ObservableAndroidViewModel
import com.ajax.ajaxtestassignment.base.Result
import com.ajax.ajaxtestassignment.db.AppDatabase
import com.ajax.ajaxtestassignment.repos.ContactsRepository
import com.ajax.ajaxtestassignment.repos.ContactsRepositoryImpl

class ContactsViewModel constructor(
    val app: Application,
    var contactsRepository: ContactsRepository
) :
    ObservableAndroidViewModel(app) {

    fun getContacts(): LiveData<Result<MutableList<ContactEntity>>> {
        val cm =
            app.baseContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var activeNetworkInfo: NetworkInfo? = cm.activeNetworkInfo
        var isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnected
        return contactsRepository.getContacts(isConnected)
    }

    class Factory constructor(var application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return ContactsViewModel(
                application,
                ContactsRepositoryImpl(
                    RetrofitServicesProvider().contactsService,
                    AppDatabase.getAppDataBase(application.baseContext)
                )
            ) as T
        }
    }
}