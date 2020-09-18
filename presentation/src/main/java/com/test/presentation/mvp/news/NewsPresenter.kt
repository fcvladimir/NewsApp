package com.test.presentation.mvp.news

import com.test.domain.model.New
import com.test.domain.usecase.favorite.DispatchNewToFavoritesUc
import com.test.domain.usecase.news.FetchNewsUc
import com.test.domain.usecase.news.SaveImageUc

class NewsPresenter(
        private val fetchNews: FetchNewsUc,
        private val dispatchNewToFavorites: DispatchNewToFavoritesUc,
        private val saveImage: SaveImageUc
) : INewsContract.Presenter {

    private var page = 1
    private var pageSize = 20
    private var sources: String? = null
    private var fromDate = ""
    private var toDate = ""
    private var sortBy = ""

    override var view: INewsContract.View? = null

    override fun refreshNews() {
        fetchNews.execute(
                onSuccess = {
                    view?.renderNetworkProcessing(false)
                    if (it.isNotEmpty()) {
                        this@NewsPresenter.page = 2
                    }
                    view?.showNews(it, true)
                },
                doOnSubscribe = {
                    view?.renderNetworkProcessing(true)
                },
                onError = {
                    view?.renderNetworkProcessing(false)
                    view?.showError(it.message)
                },
                param = FetchNewsUc.Param(1, pageSize, sources, fromDate, toDate, sortBy)
        )
    }

    override fun getNews() {
        fetchNews.execute(
                onSuccess = {
                    view?.renderNetworkProcessing(false)
                    if (it.isNotEmpty()) {
                        this@NewsPresenter.page.inc()
                    }
                    view?.showNews(it, false)
                },
                doOnSubscribe = {
                    view?.renderNetworkProcessing(true)
                },
                onError = {
                    view?.renderNetworkProcessing(false)
                    view?.showError(it.message)
                },
                param = FetchNewsUc.Param(this@NewsPresenter.page, pageSize, sources, fromDate, toDate, sortBy)
        )
    }

    override fun onFromDateSelected(date: String) {
        this@NewsPresenter.fromDate = date
        view?.renderFromDate(this@NewsPresenter.fromDate)
    }

    override fun onToDateSelected(date: String) {
        this@NewsPresenter.toDate = date
        view?.renderToDate(this@NewsPresenter.toDate)
    }

    override fun onFromDateClick() {
        view?.renderFromDatePicker(this@NewsPresenter.fromDate)
    }

    override fun onToDateClick() {
        view?.renderToDatePicker(this@NewsPresenter.toDate)
    }

    override fun onSortSelected(sortValue: String, sort: String) {
        this@NewsPresenter.sortBy = sortValue
        view?.updateTitle(sort)
    }

    override fun onFilterSelected(filterValue: ArrayList<String>) {
        if (filterValue.isEmpty()) {
            this@NewsPresenter.sources = null
        } else {
            this@NewsPresenter.sources = filterValue.joinToString(separator = ",")
        }
        view?.updateSubtitle(this@NewsPresenter.sources)
    }

    override fun onFavoritesActionClick() {
        view?.navigateToFavoritesScreen()
    }

    override fun onSortActionClick() {
        view?.renderSortByDialog()
    }

    override fun onFilterActionClick() {
        view?.renderFilterDialog()
    }

    override fun onNewClick(new: New) {
        dispatchNewToFavorites.execute(
                onSuccess = {
                    view?.renderNetworkProcessing(false)
                    view?.showNewCachedMessage()
                },
                doOnSubscribe = {
                    view?.renderNetworkProcessing(true)
                },
                onError = {
                    view?.renderNetworkProcessing(false)
                    view?.showError(it.message)
                },
                param = DispatchNewToFavoritesUc.Param(new)
        )
    }

    override fun onNewImageSaveClick(imageUrl: String) {
        saveImage.execute(
                onSuccess = {
                    view?.renderNetworkProcessing(false)
                    view?.updateGalleryInfo(it)
                },
                doOnSubscribe = {
                    view?.renderNetworkProcessing(true)
                },
                onError = {
                    view?.renderNetworkProcessing(false)
                    view?.showError(it.message)
                },
                param = SaveImageUc.Param(imageUrl)
        )
    }

    override fun onWhiteExternalStoragePermissionGranted() {
        view?.showWhiteExternalStoragePermissionGrantedMessage()
    }

    override fun onWhiteExternalStoragePermissionDenied() {
        view?.showWhiteExternalStoragePermissionDeniedMessage()
    }

    override fun onUnsubscribe() {
        fetchNews.clear()
        dispatchNewToFavorites.clear()
        saveImage.clear()
    }

    override fun onDestroy() {
        fetchNews.dispose()
        dispatchNewToFavorites.dispose()
        saveImage.dispose()
    }
}