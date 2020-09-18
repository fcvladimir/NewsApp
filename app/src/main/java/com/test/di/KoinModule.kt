package com.test.di

import com.google.gson.Gson
import com.test.data.ImageRepository
import com.test.data.NewsRepository
import com.test.data.datasources.IImageDS
import com.test.data.datasources.INewsDS
import com.test.data.mapper.DataMapper
import com.test.domain.IRepository
import com.test.domain.model.New
import com.test.domain.usecase.favorite.DispatchNewToFavoritesUc
import com.test.domain.usecase.favorite.FetchFavoriteNewsUc
import com.test.domain.usecase.news.FetchNewsUc
import com.test.domain.usecase.news.SaveImageUc
import com.test.local.LocalMapper
import com.test.local.db.LocalNewsDS
import com.test.navigation.NewsRouter
import com.test.presentation.mvp.favorites.FavoritesPresenter
import com.test.presentation.mvp.favorites.IFavoritesContract
import com.test.presentation.mvp.news.INewsContract
import com.test.presentation.mvp.news.NewsPresenter
import com.test.presentation.routers.INewsRouter
import com.test.remote.RemoteImageDS
import com.test.remote.RemoteNewsDS
import com.test.remote.mapper.RemoteMapper
import com.test.remote.retrofit.RetrofitServiceFactory
import com.test.ui.activity.BaseActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import org.koin.dsl.module.module

val mainModule = module {

    single { AndroidSchedulers.mainThread() }
    factory { CompositeDisposable() }

    single { Gson() }

    single { RetrofitServiceFactory() }

    /*
    * Remote RetrofitServices
    * */
    single { get<RetrofitServiceFactory>().createService() }

    /*
    * Database
    * */
//    single { NewsDatabase.getInstance(get()) }
//    single { Room.databaseBuilder(get(), NewsDatabase::class.java, "news_database").build() }
//    single { get<NewsDatabase>().newsDao() }

    /*
    * Mappers
    * */
    single { DataMapper() }
    single { RemoteMapper() }
    single { LocalMapper() }

    /*
    * News DS
    * */
    single<INewsDS>(name = KOIN_NAME_DS_NEWS_REMOTE) { RemoteNewsDS(get(), get()) }

    /*
    * Local News DS
    * */
    single<INewsDS>(name = KOIN_NAME_DS_NEWS_LOCAL) {
        LocalNewsDS(get())
    }
    /*
    * Remote Image DS
    * */
    single<IImageDS>(name = KOIN_NAME_DS_IMAGE_REMOTE) {
        RemoteImageDS()
    }

    /*
    * Repositories
    * */
    factory<IRepository<List<New>>>(name = KOIN_NAME_REPOSITORY_NEWS) {
        NewsRepository(
                get(name = KOIN_NAME_DS_NEWS_REMOTE),
                get(name = KOIN_NAME_DS_NEWS_LOCAL),
                get()
        )
    }
    factory<IRepository<String>>(name = KOIN_NAME_REPOSITORY_IMAGE) {
        ImageRepository(
                get(name = KOIN_NAME_DS_IMAGE_REMOTE)
        )
    }

    /*
    * UseCases
    * */
    factory(name = FetchNewsUc::class.java.name) {
        FetchNewsUc(
                get(),
                get(),
                get(KOIN_NAME_REPOSITORY_NEWS)
        )
    }
    factory(name = FetchFavoriteNewsUc::class.java.name) {
        FetchFavoriteNewsUc(
                get(),
                get(),
                get(KOIN_NAME_REPOSITORY_NEWS)
        )
    }
    factory(name = DispatchNewToFavoritesUc::class.java.name) {
        DispatchNewToFavoritesUc(
                get(),
                get(),
                get(KOIN_NAME_REPOSITORY_NEWS)
        )
    }
    factory(name = SaveImageUc::class.java.name) {
        SaveImageUc(
                get(),
                get(),
                get(KOIN_NAME_REPOSITORY_IMAGE)
        )
    }

    /*
    * Routers
    * */
    scope<INewsRouter>(scopeId = KOIN_KEY_SCOPE_NEWS_ACTIVITY) { (activity: BaseActivity) -> NewsRouter(activity) }

    /*
    * Presenters
    * */
    scope<INewsContract.Presenter>(scopeId = KOIN_KEY_SCOPE_NEWS_ACTIVITY) {
        NewsPresenter(get(FetchNewsUc::class.java.name), get(DispatchNewToFavoritesUc::class.java.name), get(SaveImageUc::class.java.name))
    }
    scope<IFavoritesContract.Presenter>(scopeId = KOIN_KEY_SCOPE_FAVORITES_ACTIVITY) {
        FavoritesPresenter(get(FetchFavoriteNewsUc::class.java.name))
    }

    scope(KOIN_KEY_SCOPE_NEWS_ACTIVITY) {
        single { KOIN_KEY_SCOPE_NEWS_ACTIVITY }
    }

    scope(KOIN_KEY_SCOPE_FAVORITES_ACTIVITY) {
        single { KOIN_KEY_SCOPE_FAVORITES_ACTIVITY }
    }
}

enum class ScopeName {
    KOIN_KEY_SCOPE_NEWS_ACTIVITY,
    KOIN_KEY_SCOPE_FAVORITES_ACTIVITY
}

val KOIN_KEY_SCOPE_NEWS_ACTIVITY = ScopeName.KOIN_KEY_SCOPE_NEWS_ACTIVITY.name
val KOIN_KEY_SCOPE_FAVORITES_ACTIVITY = ScopeName.KOIN_KEY_SCOPE_FAVORITES_ACTIVITY.name

val KOIN_NAME_REPOSITORY_NEWS = NewsRepository::class.java.name
val KOIN_NAME_REPOSITORY_IMAGE = ImageRepository::class.java.name

val KOIN_NAME_DS_NEWS_REMOTE = RemoteNewsDS::class.java.name
val KOIN_NAME_DS_NEWS_LOCAL = LocalNewsDS::class.java.name
val KOIN_NAME_DS_IMAGE_REMOTE = RemoteImageDS::class.java.name