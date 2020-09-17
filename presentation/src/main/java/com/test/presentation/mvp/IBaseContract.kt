package com.test.presentation.mvp

interface IBaseContract {

    interface View

    interface Presenter<V : View> {
        var view: V?

        fun onUnsubscribe()
        fun onDestroy()
    }

}