package com.test.remote.retrofit

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import com.photobutler.test.remote.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitServiceFactory {

    fun createService(): IRemoteService {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.WEB_SERVER)
                .client(
                        buildOkHttp(
                                HttpLoggingInterceptor()
                                        .apply {
                                            level = if (BuildConfig.DEBUG) {
                                                HttpLoggingInterceptor.Level.BODY
                                            } else {
                                                HttpLoggingInterceptor.Level.NONE
                                            }
                                        }
                        )
                                .build()
                )
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build().create(IRemoteService::class.java)
    }

    private fun buildOkHttp(vararg interceptor: Interceptor): OkHttpClient.Builder {
        return OkHttpClient.Builder()
                .apply {
                    interceptor.forEach {
                        addInterceptor(it)
                    }
                }
    }
}