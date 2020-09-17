package com.test.navigation

import com.test.presentation.routers.INewsRouter
import com.test.ui.activity.BaseActivity
import com.test.ui.activity.favorites.FavoritesActivity

class NewsRouter(private val activity: BaseActivity) : INewsRouter {

    override fun navigateToFavoritesScreen() {
        activity.startActivity(FavoritesActivity.instance(activity))
    }

}