package com.udacity.asteroidradar.api

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val okHttpClient = OkHttpClient.Builder()
    .addInterceptor { chain ->
    // to make all requests contains the api key
    val original = chain.request()
    val url = original.url().newBuilder()
        .addQueryParameter("api_key", Constants.API_KEY)
        .build()

    val new = original.newBuilder().url(url).build()
    chain.proceed(new)
}.build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create()) // to be able to convert to string
    .addConverterFactory(MoshiConverterFactory.create(moshi)) // to be able to convert to kotlin objects
    .addCallAdapterFactory(CoroutineCallAdapterFactory()) // to be able to return a Deferred
    .client(okHttpClient)
    .baseUrl(Constants.BASE_URL)
    .build()


interface NeoWsApiService {
    @GET("neo/rest/v1/feed")
    fun getAsteroidsAsync(): Deferred<String>

    @GET("planetary/apod")
    fun getPicOfTheDayAsync(): Deferred<PictureOfDay>
}

object NeoWsApi {
    val retrofitService: NeoWsApiService by lazy {
        retrofit.create(NeoWsApiService::class.java)
    }
}