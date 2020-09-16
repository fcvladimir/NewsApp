package com.test.domain.usecase.base

import io.reactivex.Completable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

abstract class AbsCompletableUc<P>(
        compositeDisposable: CompositeDisposable,
        protected val postExecutionScheduler: Scheduler
) : UseCase(compositeDisposable) {

    abstract fun buildUc(param: P): Completable

    fun execute(
            onSuccess: () -> Unit,
            param: P,
            onError: ((Throwable) -> Unit)? = null,
            doOnSubscribe: (() -> Unit)? = null,
            doOnDispose: (() -> Unit)? = null
    ) {

        scheduleUc(param)
                .doOnSubscribe {
                    doOnSubscribe?.invoke()
                }.doOnDispose {
                    doOnDispose?.invoke()
                }.subscribe({
                    onSuccess.invoke()
                }, {
                    onError?.invoke(it)
                    it.printStackTrace()
                }).addTo(compositeDisposable)
    }

    private fun scheduleUc(param: P) =
            buildUc(param)
                    .observeOn(postExecutionScheduler)

}