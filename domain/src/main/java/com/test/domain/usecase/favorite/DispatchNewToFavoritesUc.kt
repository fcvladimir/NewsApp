package com.test.domain.usecase.favorite

import com.test.domain.IRepository
import com.test.domain.model.New
import com.test.domain.usecase.base.AbsCompletableUc
import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable

class DispatchNewToFavoritesUc(
        compositeDisposable: CompositeDisposable,
        scheduler: Scheduler,
        private val newsRepository: IRepository<List<New>>
) : AbsCompletableUc<DispatchNewToFavoritesUc.Param>(compositeDisposable, scheduler) {

    override fun buildUc(param: Param): Completable {
        return newsRepository.update(IRepository.Update.DispatchNewToFavorites(param.new))
    }

    data class Param(val new: New)
}