package com.ajax.ajaxtestassignment.api

import com.ajax.ajaxtestassignment.api.contacts.ContactsService
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitServicesProvider {
    private val moshi: Moshi = Moshi.Builder()
        .build()

    private val client = OkHttpClient.Builder()
        .readTimeout(30, TimeUnit.SECONDS)
        .connectTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY })
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://randomuser.me/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(client)
        .build()

    val contactsService: ContactsService
        get() = retrofit.create(ContactsService::class.java)
}