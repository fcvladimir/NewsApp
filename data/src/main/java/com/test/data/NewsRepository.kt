package com.test.data

import com.test.data.datasources.INewsDS
import com.test.data.mapper.DataMapper
import com.test.domain.IRepository
import com.test.domain.model.New
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class NewsRepository(
        private val remoteNewsDs: INewsDS,
        private val localNewsDs: INewsDS,
        private val mapper: DataMapper
) : IRepository<List<New>> {

    override fun <P : IRepository.Get<List<New>>> get(param: P): Single<List<New>> {
        return when (param) {
            is IRepository.Get.FetchNews -> fetchNews(param.page, param.pageSize, param.sources, param.fromDate, param.toDate, param.sortBy)
            is IRepository.Get.FetchFavoriteNews -> fetchFavoriteNews()
            else -> throw UnsupportedOperationException("${param::class.java.name} operation is unsupported")
        }
    }

    override fun <P : IRepository.Update<List<New>>> update(param: P): Completable {
        return when (param) {
            is IRepository.Update.DispatchNewToFavorites -> dispatchNewToFavorites(param.new)
            else -> throw UnsupportedOperationException("${param::class.java.name} operation is unsupported")
        }
    }

    override fun <P : IRepository.Delete> delete(param: P): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <P : IRepository.Observe> observe(param: P): Observable<List<New>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun fetchNews(page: Int, pageSize: Int, sources: String?, fromDate: String, toDate: String, sortBy: String) =
            remoteNewsDs.fetchNews(page, pageSize, sources, fromDate, toDate, sortBy)
                    .map { mapper.mapFromNewEntity(it) }

    private fun fetchFavoriteNews() =
            localNewsDs.fetchFavoriteNews()
                    .map { mapper.mapFromNewEntity(it) }

    private fun dispatchNewToFavorites(new: New) =
            localNewsDs.dispatchNewToFavorites(mapper.mapToNewEntity(new))

}