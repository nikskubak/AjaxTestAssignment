package com.ajax.ajaxtestassignment.db.contacts

import androidx.room.*

@Dao
interface ContactsDao {
    @Query("SELECT * FROM Contact")
    suspend fun getContacts(): List<DbContact>

    @Update
    suspend fun update(contact: DbContact)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAll(contact: List<DbContact>?)

    @Query("DELETE FROM Contact WHERE id = (:contactId)")
    fun deleteById(contactId: Int) : Int

    @Query("DELETE FROM Contact")
    suspend fun deleteAll()
}