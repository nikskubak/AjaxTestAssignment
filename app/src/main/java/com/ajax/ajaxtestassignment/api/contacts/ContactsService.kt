package com.ajax.ajaxtestassignment.api.contacts

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ContactsService {
    @GET("api/")
    fun getContacts(@Query("results") limit: Int = 30): Call<ApiContactResponse>
}