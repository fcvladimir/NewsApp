package com.test.domain.usecase.base

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo

abstract class AbsObsevableUc<P, V>(
        compositeDisposable: CompositeDisposable,
        private val postExecutionScheduler: Scheduler
) : UseCase(compositeDisposable) {

    abstract fun buildUc(param: P): Observable<V>

    fun execute(
            onNext: (V) -> Unit,
            param: P,
            onComplete: (() -> Unit)? = null,
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
                    onNext.invoke(it)
                }, {
                    onError?.invoke(it)
                    it.printStackTrace()
                }, {
                    onComplete?.invoke()
                }).addTo(compositeDisposable)
    }

    private fun scheduleUc(param: P) =
            buildUc(param)
                    .observeOn(postExecutionScheduler)

}
