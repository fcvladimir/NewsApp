package com.test.domain.usecase.news

import com.test.domain.IRepository
import com.test.domain.usecase.base.AbsSingleUc
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class SaveImageUc(
        compositeDisposable: CompositeDisposable,
        scheduler: Scheduler,
        private val imageRepository: IRepository<String>
) : AbsSingleUc<SaveImageUc.Param, String>(compositeDisposable, scheduler) {

    override fun buildUc(param: Param): Single<String> {
        return imageRepository.get(IRepository.Get.SaveImage(param.imageUrl))
    }

    data class Param(val imageUrl: String)
}