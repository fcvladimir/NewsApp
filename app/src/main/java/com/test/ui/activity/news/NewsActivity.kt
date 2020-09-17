package com.test.ui.activity.news

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.test.R
import com.test.di.KOIN_KEY_SCOPE_NEWS_ACTIVITY
import com.test.domain.model.New
import com.test.presentation.mvp.news.INewsContract
import com.test.presentation.routers.INewsRouter
import com.test.ui.activity.BaseActivity
import com.test.ui.adapter.NewsAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.jetbrains.anko.alert
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast
import org.joda.time.DateTime
import org.koin.android.ext.android.inject
import org.koin.android.scope.ext.android.bindScope
import org.koin.android.scope.ext.android.getOrCreateScope
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.*

class NewsActivity : BaseActivity(), INewsContract.View, NewsAdapter.NewsAdapterListener {

    private val presenter by inject<INewsContract.Presenter>()
    private val router by inject<INewsRouter> { parametersOf(this) }

    lateinit var newsAdapter: NewsAdapter

    var SIMPLE_DATE_FORMAT: SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    var isLoading = false

    private var fromDateListener = OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        val day = String.format("%02d", dayOfMonth)
        val month = String.format("%02d", monthOfYear + 1)
        presenter.onFromDateSelected("${year}-${month}-${day}")
    }

    private var toDateListener = OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
        val day = String.format("%02d", dayOfMonth)
        val month = String.format("%02d", monthOfYear + 1)
        presenter.onToDateSelected("${year}-${month}-${day}")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bindScope(getOrCreateScope(KOIN_KEY_SCOPE_NEWS_ACTIVITY))

        presenter.view = this

        setSupportActionBar(toolbar)

        fillDates()
        initAdapter()

        btnFromDate.onClick { presenter.onFromDateClick() }
        btnToDate.onClick { presenter.onToDateClick() }

        val sortValueArray = resources.getStringArray(R.array.sort_value_name)
        val sortArray = resources.getStringArray(R.array.sort_values)
        presenter.onSortSelected(sortValueArray[0], sortArray[0])
        presenter.onFilterSelected(arrayListOf())

        presenter.refreshNews()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorites_action -> presenter.onFavoritesActionClick()
            R.id.sort_action -> presenter.onSortActionClick()
            R.id.filter_action -> presenter.onFilterActionClick()
            else -> return false
        }
        return true
    }

    private fun fillDates() {
        val fromDate = SIMPLE_DATE_FORMAT.format(DateTime().minusMonths(1).toCalendar(Locale.getDefault()).time)
        val toDate = SIMPLE_DATE_FORMAT.format(DateTime().toCalendar(Locale.getDefault()).time)
        presenter.onFromDateSelected(fromDate)
        presenter.onToDateSelected(toDate)
    }

    private fun initAdapter() {
        newsAdapter = NewsAdapter(this)
        rvNews.adapter = newsAdapter
        rvNews.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        val lm = LinearLayoutManager(this)
        rvNews.layoutManager = lm
        rvNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (isLoading) return
                val visibleItemCount: Int = lm.childCount
                val totalItemCount: Int = lm.itemCount
                val pastVisibleItems: Int = lm.findFirstVisibleItemPosition()
                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
                    presenter.getNews()
                }
            }
        })
    }

    override fun renderNetworkProcessing(processing: Boolean) {
        isLoading = processing
    }

    override fun renderFromDatePicker(date: String) {
        val cal = Calendar.getInstance()
        cal.time = SIMPLE_DATE_FORMAT.parse(date)!!
        DatePickerDialog(this, fromDateListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH))
                .show()
    }

    override fun renderToDatePicker(date: String) {
        DatePickerDialog(this, toDateListener,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH))
                .show()
    }

    override fun renderFromDate(date: String) {
        btnFromDate.text = date
    }

    override fun renderToDate(date: String) {
        btnToDate.text = date
    }

    override fun renderSortByDialog() {
        val sortArray = resources.getStringArray(R.array.sort_values).toMutableList()
        alert {
            title = getString(R.string.sort_dialog_title)
            items(sortArray
            ) { _, i ->
                val sortValueArray = resources.getStringArray(R.array.sort_value_name)
                presenter.onSortSelected(sortValueArray[i], sortArray[i])
                presenter.refreshNews()
            }
        }.show()
    }

    override fun renderFilterDialog() {
        val filterArray = resources.getStringArray(R.array.filter_values)
        val filterValueArray = resources.getStringArray(R.array.filter_value_name)
        val filterResult = arrayListOf<String>()

        AlertDialog.Builder(this)
                .setTitle(getString(R.string.filter_dialog_title))
                .setMultiChoiceItems(filterArray, null
                ) { _, item, isChecked ->
                    if (isChecked) {
                        filterResult.add(filterValueArray[item])
                    } else {
                        if (filterResult.contains(filterValueArray[item])) {
                            filterResult.remove(filterValueArray[item])
                        }
                    }
                }
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    presenter.onFilterSelected(filterResult)
                    presenter.refreshNews()
                }
                .create().show()
    }

    override fun showNews(news: List<New>, refresh: Boolean) {
        if (refresh)
            newsAdapter.newsList.clear()
        newsAdapter.newsList.addAll(news)
        newsAdapter.notifyDataSetChanged()
    }

    override fun updateTitle(sort: String) {
        title = getString(R.string.app_name) + " (${sort})"
    }

    override fun updateSubtitle(filter: String?) {
        toolbar.subtitle = filter ?: getString(R.string.subtitle_no_filter)
    }

    override fun navigateToFavoritesScreen() {
        router.navigateToFavoritesScreen()
    }

    override fun showError(message: String?) {
        toast(message ?: getString(R.string.default_error_message))
    }

    override fun onDestroy() {
        presenter.view = null
        presenter.onUnsubscribe()
        presenter.onDestroy()
        super.onDestroy()
    }

    override fun onNewClick(new: New) {
        presenter.onNewClick(new)
    }
}
