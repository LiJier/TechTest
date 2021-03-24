package com.lijie.techtest.http

import com.lijie.techtest.BuildConfig
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.CookieManager
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Create by LiJie at 2021年03月24日
 * 网络请求，支持不同baseUrl
 */
object ApiClient {

    var hasInit = false
        private set

    lateinit var baseUrl: String
        private set

    lateinit var okHttpClient: OkHttpClient
        private set

    lateinit var retrofit: Retrofit
        private set

    var apiMap = HashMap<String, Any>()
        private set

    /**
     * 初始化
     */
    fun init(
        baseUrl: String,
        okHttpClient: OkHttpClient? = null,
        retrofit: Retrofit? = null
    ): ApiClient {
        ApiClient.baseUrl = baseUrl
        okHttpClient?.let {
            this.okHttpClient = it
        } ?: run {
            this.okHttpClient = defaultOkHttp()
        }
        retrofit?.let {
            this.retrofit = it
        } ?: run {
            this.retrofit = defaultRetrofit(baseUrl)
        }
        hasInit = true
        return this
    }

    /**
     * 默认okHttp
     */
    private fun defaultOkHttp(): OkHttpClient {
        return OkHttpClient.Builder()
            .cookieJar(JavaNetCookieJar(CookieManager()))
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().setLevel(if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE))
            .build()
    }

    /**
     * 默认Retrofit
     */
    private fun defaultRetrofit(baseUrl: String): Retrofit {
        if (ApiClient::okHttpClient.isInitialized.not()) {
            okHttpClient = defaultOkHttp()
        }
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .callFactory(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    /**
     * 获取接口
     */
    inline fun <reified T> api(): T {
        val name = T::class.java.name
        return if (apiMap.containsKey(name)) {
            apiMap[name] as T
        } else {
            val api = retrofit.create(T::class.java)
            apiMap.put(name, api!!)
            api
        }
    }

}