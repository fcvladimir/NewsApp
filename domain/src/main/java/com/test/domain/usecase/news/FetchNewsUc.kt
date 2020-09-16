package com.test.domain.usecase.news

import com.test.domain.IRepository
import com.test.domain.model.New
import com.test.domain.usecase.base.AbsSingleUc
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class FetchNewsUc(
        compositeDisposable: CompositeDisposable,
        scheduler: Scheduler,
        private val newsRepository: IRepository<List<New>>
) : AbsSingleUc<FetchNewsUc.Param, List<New>>(compositeDisposable, scheduler) {

    override fun buildUc(param: Param): Single<List<New>> {
        return newsRepository.get(IRepository.Get.FetchNews(param.page, param.pageSize, param.sources, param.fromDate, param.toDate, param.sortBy))
    }

    data class Param(val page: Int, val pageSize: Int, val sources: String?, val fromDate: String, val toDate: String, val sortBy: String)
}