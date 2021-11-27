package com.ajax.ajaxtestassignment.repos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ajax.ajaxtestassignment.api.contacts.ApiContact
import com.ajax.ajaxtestassignment.api.contacts.ApiContactResponse
import com.ajax.ajaxtestassignment.api.contacts.ContactsService
import com.ajax.ajaxtestassignment.base.Result
import com.ajax.ajaxtestassignment.db.AppDatabase
import com.ajax.ajaxtestassignment.db.contacts.DbContact
import com.ajax.ajaxtestassignment.ui.contactslist.ContactEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ContactsRepositoryImpl constructor(
    var network: ContactsService,
    var database: AppDatabase?
) : ContactsRepository {

    private suspend fun retrieveEntitiesFromNetwork(): MutableList<ContactEntity>? {
        return withContext(Dispatchers.IO) {
            val response = network.getContacts().execute()
            val contactsEntities =
                if (response.isSuccessful) (response.body() as ApiContactResponse)
                    .results
                    ?.map { mapToEntity(it) }
                    ?.toMutableList()
                else mutableListOf()
            saveEntitiesToDatabase(contactsEntities)
            contactsEntities
        }
    }

    private suspend fun retrieveEntitiesFromDatabase(): MutableList<ContactEntity>? {
        return withContext(Dispatchers.IO) {
            database
                ?.userDao()
                ?.getContacts()
                ?.map { mapToEntity(it) }
                ?.toMutableList()
        }
    }

    private suspend fun saveEntitiesToDatabase(entities: MutableList<ContactEntity>?) {
        withContext(Dispatchers.IO) {
            database?.userDao()?.addAll(entities?.map { mapToDbContact(it) })
        }
    }

    override fun getContacts(isConnected: Boolean): LiveData<Result<MutableList<ContactEntity>>> {
        val resultLivaData = MutableLiveData<Result<MutableList<ContactEntity>>>()
        val result = Result<MutableList<ContactEntity>>()
        val job = CoroutineScope(Dispatchers.Main).launch {
            try {
                val list =
                    if (isConnected) retrieveEntitiesFromNetwork() else retrieveEntitiesFromDatabase()
                result.data = list
            } catch (e: Exception) {
                result.error = e
            } finally {
                resultLivaData.value = result
            }
        }
        return resultLivaData
    }

    override fun deleteContact(id: Int): LiveData<Result<Int>> {
        val resultLivaData = MutableLiveData<Result<Int>>()
        val result = Result<Int>()
        val job = CoroutineScope(Dispatchers.Main).launch {
            try {
                withContext(Dispatchers.IO) {
                    result.data = database?.userDao()?.deleteById(id)
                }
            } catch (e: Exception) {
                result.error = e
            } finally {
                resultLivaData.value = result
            }
        }
        return resultLivaData
    }

    override fun updateContact(contactEntity: ContactEntity): LiveData<Result<Int>> {
        val resultLivaData = MutableLiveData<Result<Int>>()
        val result = Result<Int>()
        return resultLivaData
    }

    private fun mapToEntity(apiContact: ApiContact): ContactEntity {
        return ContactEntity(
            firstName = apiContact.name?.firstName,
            lastName = apiContact.name?.lastName,
            email = apiContact.email,
            photo = apiContact.picture?.medium
        )
    }

    private fun mapToEntity(dbContact: DbContact): ContactEntity {
        return ContactEntity(
            firstName = dbContact.firstName,
            lastName = dbContact.lastName,
            email = dbContact.email,
            photo = dbContact.photo
        )
    }

    private fun mapToDbContact(contactEntity: ContactEntity): DbContact {
        return DbContact(
            firstName = contactEntity.firstName,
            lastName = contactEntity.lastName,
            email = contactEntity.email,
            photo = contactEntity.photo
        )
    }
}