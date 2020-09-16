package com.test.domain.usecase.base

import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

abstract class AbsSingleUc<P, V>(
        compositeDisposable: CompositeDisposable,
        protected val postExecutionScheduler: Scheduler
) : UseCase(compositeDisposable) {

    abstract fun buildUc(param: P): Single<V>

    fun execute(
            onSuccess: (V) -> Unit,
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
                    onSuccess.invoke(it)
                }, {
                    onError?.invoke(it)
                    it.printStackTrace()
                }).addTo(compositeDisposable)
    }

    private fun scheduleUc(param: P) =
            buildUc(param)
                    .observeOn(postExecutionScheduler)

}