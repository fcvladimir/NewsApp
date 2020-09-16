package com.test.presentation

interface IBaseContract {

    interface View

    interface Presenter<V : View> {
        var view: V?

        fun onUnsubscribe()
        fun onDestroy()
    }

}