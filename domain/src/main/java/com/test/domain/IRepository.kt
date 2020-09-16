package com.test.domain

import com.test.domain.model.New
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

interface IRepository<V> {

    fun <P : Get<V>> get(param: P): Single<V>

    fun <P : Update<V>> update(param: P): Completable

    fun <P : Delete> delete(param: P): Completable

    fun <P : Observe> observe(param: P): Observable<V>

    interface Get<V> {

        /**
         * Fetch News
         */
        class FetchNews(val page: Int, val pageSize: Int, val sources: String?, val fromDate: String, val toDate: String, val sortBy: String) : Get<List<New>>
    }

    interface Update<V>

    interface Delete

    interface Observe

    class AuthError(message: String = "") : Exception(message)
    class ApiError(message: String = "") : Exception(message)
    class NoItemsFoundException(message: String = "") : Exception(message)
}

