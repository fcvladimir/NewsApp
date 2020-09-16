package com.test.data.datasources

import com.test.data.model.NewEntity
import io.reactivex.Single

interface INewsDS {

    fun fetchNews(page: Int, pageSize: Int, sources: String?, fromDate: String, toDate: String, sortBy: String): Single<List<NewEntity>> {
        return Single.error(UnsupportedOperationException("This DS implementation does not support fetchNews() function"))
    }
}