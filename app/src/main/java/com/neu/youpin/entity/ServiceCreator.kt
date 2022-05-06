package com.neu.youpin.entity

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// 创建Retrofit对象
object ServiceCreator {
    private const val BASE_URL = "http://hqyz.cf:8080/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
    inline fun <reified T> create(): T = create(T::class.java)
}