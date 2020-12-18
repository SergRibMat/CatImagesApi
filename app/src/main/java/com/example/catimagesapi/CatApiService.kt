package com.example.catimagesapi

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


const val ROOT_URL = "https://api.thecatapi.com/v1/images/"


private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(ROOT_URL)
    .build()

interface CatApiService {
    @GET("search")
    suspend fun getProperties():
            List<CatObjectApi>
}

//object CatApi {
    var retrofitService: CatApiService =
        retrofit.create(CatApiService::class.java)

//}


