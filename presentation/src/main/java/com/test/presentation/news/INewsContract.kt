package com.test.presentation.news

import com.test.domain.model.New
import com.test.presentation.IBaseContract

interface INewsContract {

    interface View : IBaseContract.View {

        fun renderNetworkProcessing(processing: Boolean)

        fun renderFromDatePicker(date: String)

        fun renderToDatePicker(date: String)

        fun renderFromDate(date: String)

        fun renderToDate(date: String)

        fun renderSortByDialog()

        fun renderFilterDialog()

        fun showNews(news: List<New>, refresh: Boolean)

        fun updateTitle(sort: String)

        fun updateSubtitle(filter: String?)
    }

    interface Presenter : IBaseContract.Presenter<View> {

        fun refreshNews()

        fun getNews()

        fun onFromDateSelected(date: String)

        fun onToDateSelected(date: String)

        fun onFromDateClick()

        fun onToDateClick()

        fun onSortSelected(sortValue: String, sort: String)

        fun onFilterSelected(filterValue: ArrayList<String>)

        fun onSortActionClick()

        fun onFilterActionClick()
    }
}