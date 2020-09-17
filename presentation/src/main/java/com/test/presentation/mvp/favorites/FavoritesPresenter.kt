package com.test.presentation.mvp.favorites

import com.test.domain.usecase.favorite.FetchFavoriteNewsUc

class FavoritesPresenter(
        private val fetchFavoriteNews: FetchFavoriteNewsUc
) : IFavoritesContract.Presenter {

    override var view: IFavoritesContract.View? = null

    override fun getFavoriteNews() {
        fetchFavoriteNews.execute(
                onSuccess = {
                    view?.renderNetworkProcessing(false)
                    view?.showNews(it)
                },
                doOnSubscribe = {
                    view?.renderNetworkProcessing(true)
                },
                onError = {
                    view?.renderNetworkProcessing(false)
                    when (it) {
                        is NoSuchElementException -> view?.showNoNewsError()
                        else -> view?.showError(it.message)
                    }
                },
                param = Unit
        )
    }

    override fun onUnsubscribe() {
        fetchFavoriteNews.clear()
    }

    override fun onDestroy() {
        fetchFavoriteNews.dispose()
    }
}