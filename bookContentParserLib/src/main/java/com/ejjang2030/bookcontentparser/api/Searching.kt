package com.ejjang2030.bookcontentparser.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

abstract class Searching {
    fun <T> getCallback(
        callback: ((Call<T>, retrofit2.Response<T>?, Throwable?) -> Unit)? = null
    ): Callback<T> {
        return object: Callback<T> {
            override fun onResponse(call: Call<T>, response: retrofit2.Response<T>) {
                callback?.invoke(call, response, null)
            }
            override fun onFailure(call: Call<T>, t: Throwable) {
                callback?.invoke(call, null, t)
            }
        }
    }

    fun provideOkHttpClient(
        interceptor: Interceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .run {
            addInterceptor(interceptor)
            build()
        }

    fun createRetrofitBuilderWithURL(url: String, interceptor: Interceptor? = null): Retrofit {
        val retrofit = Retrofit.Builder().baseUrl(url)
        if(interceptor != null) {
            retrofit.client(provideOkHttpClient(interceptor))
        }
        return retrofit.addConverterFactory(GsonConverterFactory.create()).build()
    }
}