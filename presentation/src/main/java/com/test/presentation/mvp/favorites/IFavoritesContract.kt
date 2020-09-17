package com.test.presentation.mvp.favorites

import com.test.domain.model.New
import com.test.presentation.mvp.IBaseContract

interface IFavoritesContract {

    interface View : IBaseContract.View {

        fun renderNetworkProcessing(processing: Boolean)

        fun showNews(news: List<New>)

        fun showNoNewsError()

        fun showError(message: String?)
    }

    interface Presenter : IBaseContract.Presenter<View> {

        fun getFavoriteNews()
    }
}