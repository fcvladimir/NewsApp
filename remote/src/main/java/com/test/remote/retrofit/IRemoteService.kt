package com.test.remote.retrofit

import com.test.remote.model.BaseResponse
import com.test.remote.model.NewsJson
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface IRemoteService {

    @GET("v2/everything")
    fun fetchNews(@QueryMap fields: Map<String, String>): Single<BaseResponse<List<NewsJson>>>
}