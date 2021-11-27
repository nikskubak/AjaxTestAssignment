package com.ajax.ajaxtestassignment.repos

import androidx.lifecycle.LiveData
import com.ajax.ajaxtestassignment.ui.contactslist.ContactEntity
import com.ajax.ajaxtestassignment.base.Result

interface ContactsRepository {
    fun getContacts(isConnected : Boolean) : LiveData<Result<MutableList<ContactEntity>>>
    fun deleteContact(id : Int) : LiveData<Result<Int>>
    fun updateContact(contactEntity: ContactEntity) : LiveData<Result<Int>>
}