package com.test.presentation.news

import com.test.domain.usecase.news.FetchNewsUc

class NewsPresenter(
        private val fetchNews: FetchNewsUc
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
//                    when (it) {
//                        is NoInternetConnectionError -> {
//                            view?.showNoInternetConnectionError()
//                        }
//                        else -> view?.showServerError()
//                    }
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
//                    when (it) {
//                        is NoInternetConnectionError -> {
//                            view?.showNoInternetConnectionError()
//                        }
//                        else -> view?.showServerError()
//                    }
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

    override fun onSortActionClick() {
        view?.renderSortByDialog()
    }

    override fun onFilterActionClick() {
        view?.renderFilterDialog()
    }

    override fun onUnsubscribe() {
        fetchNews.clear()
    }

    override fun onDestroy() {
        fetchNews.dispose()
    }
}