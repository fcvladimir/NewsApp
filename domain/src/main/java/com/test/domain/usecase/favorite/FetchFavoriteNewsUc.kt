package com.test.domain.usecase.favorite

import com.test.domain.IRepository
import com.test.domain.model.New
import com.test.domain.usecase.base.AbsSingleUc
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class FetchFavoriteNewsUc(
        compositeDisposable: CompositeDisposable,
        scheduler: Scheduler,
        private val newsRepository: IRepository<List<New>>
) : AbsSingleUc<Unit, List<New>>(compositeDisposable, scheduler) {

    override fun buildUc(param: Unit): Single<List<New>> {
        return newsRepository.get(IRepository.Get.FetchFavoriteNews())
    }
}