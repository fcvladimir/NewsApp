package com.test.domain.usecase.base

import io.reactivex.disposables.CompositeDisposable

open class UseCase(protected val compositeDisposable: CompositeDisposable) {

    fun dispose() {
        if (compositeDisposable.isDisposed) return
        compositeDisposable.dispose()
    }

    fun clear() {
        if (compositeDisposable.isDisposed) return
        compositeDisposable.clear()
    }
}
