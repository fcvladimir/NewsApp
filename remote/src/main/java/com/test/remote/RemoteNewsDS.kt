package com.test.remote

import com.photobutler.test.remote.BuildConfig.NEWS_API_KEY
import com.test.data.datasources.INewsDS
import com.test.data.model.NewEntity
import com.test.remote.mapper.RemoteMapper
import com.test.remote.retrofit.IRemoteService
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class RemoteNewsDS(
        private val apiService: IRemoteService,
        private val mapper: RemoteMapper
) : INewsDS {

    override fun fetchNews(page: Int, pageSize: Int, sources: String?, fromDate: String, toDate: String, sortBy: String): Single<List<NewEntity>> {
        val params = mutableMapOf(
                "page" to page.toString(),
                "pageSize" to pageSize.toString(),
                "q" to "V-Jet",
                "fromDate" to fromDate,
                "toDate" to toDate,
                "sortBy" to sortBy,
                "apiKey" to NEWS_API_KEY
        )
        sources?.apply {
            params["sources"] = this
        }
        return apiService.fetchNews(params)
                .map {
                    mapper.mapFromNewsRemote(it.data)
                }
                .subscribeOn(Schedulers.io())
    }
}