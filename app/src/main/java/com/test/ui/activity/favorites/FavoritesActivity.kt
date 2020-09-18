package com.test.ui.activity.favorites

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import com.bumptech.glide.Glide
import com.test.R
import com.test.di.KOIN_KEY_SCOPE_FAVORITES_ACTIVITY
import com.test.domain.model.New
import com.test.presentation.mvp.favorites.IFavoritesContract
import com.test.ui.activity.BaseActivity
import com.test.ui.adapter.NewsAdapter
import kotlinx.android.synthetic.main.activity_favorites.*
import kotlinx.android.synthetic.main.app_bar_main.*
import org.jetbrains.anko.toast
import org.koin.android.ext.android.inject
import org.koin.android.scope.ext.android.bindScope
import org.koin.android.scope.ext.android.getOrCreateScope

class FavoritesActivity : BaseActivity(), IFavoritesContract.View {

    companion object {
        fun instance(activity: Activity): Intent {
            return Intent(activity, FavoritesActivity::class.java)
        }
    }

    private val presenter by inject<IFavoritesContract.Presenter>()

    private var newsAdapter: NewsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)

        bindScope(getOrCreateScope(KOIN_KEY_SCOPE_FAVORITES_ACTIVITY))

        presenter.view = this

        setSupportActionBar(toolbar)

        initAdapter()

        presenter.getFavoriteNews()
    }

    private fun initAdapter() {
        newsAdapter = NewsAdapter(Glide.with(this), null)
        rvFavoriteNews.adapter = newsAdapter
        rvFavoriteNews.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    override fun renderNetworkProcessing(processing: Boolean) {
    }

    override fun showNews(news: List<New>) {
        newsAdapter?.apply {
            newsList.clear()
            newsList.addAll(news)
            notifyDataSetChanged()
        }
    }

    override fun showNoNewsError() {
        toast(R.string.no_favorite_news_error)
    }

    override fun showError(message: String?) {
        toast(message ?: getString(R.string.default_error_message))
    }

    override fun onDestroy() {
        presenter.view = null
        presenter.onUnsubscribe()
        presenter.onDestroy()
        super.onDestroy()
        newsAdapter = null
    }
}
