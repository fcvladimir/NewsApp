package com.test.di

import com.google.gson.Gson
import com.test.data.NewsRepository
import com.test.data.datasources.INewsDS
import com.test.data.mapper.DataMapper
import com.test.domain.IRepository
import com.test.domain.model.New
import com.test.domain.usecase.news.FetchNewsUc
import com.test.presentation.news.INewsContract
import com.test.presentation.news.NewsPresenter
import com.test.remote.RemoteNewsDS
import com.test.remote.mapper.RemoteMapper
import com.test.remote.retrofit.RetrofitServiceFactory
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
    * Mappers
    * */
    single { DataMapper() }
    single { RemoteMapper() }

    /*
    * News DS
    * */
    single<INewsDS>(name = KOIN_NAME_DS_NEWS_REMOTE) { RemoteNewsDS(get(), get()) }

    /*
    * Repositories
    * */
    factory<IRepository<List<New>>>(name = KOIN_NAME_REPOSITORY_NEWS) {
        NewsRepository(
                get(name = KOIN_NAME_DS_NEWS_REMOTE),
                get()
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

    /*
    * Presenters
    * */
    scope<INewsContract.Presenter>(scopeId = KOIN_KEY_SCOPE_NEWS_ACTIVITY) {
        NewsPresenter(get(FetchNewsUc::class.java.name))
    }

    scope(KOIN_KEY_SCOPE_NEWS_ACTIVITY) {
        single { KOIN_KEY_SCOPE_NEWS_ACTIVITY }
    }

    scope(KOIN_KEY_SCOPE_NEWS_LIST_FRAGMENT) {
        single { KOIN_KEY_SCOPE_NEWS_LIST_FRAGMENT }
    }
    scope(KOIN_KEY_SCOPE_MATCHES_FRAGMENT) {
        single { KOIN_KEY_SCOPE_MATCHES_FRAGMENT }
    }
    scope(KOIN_KEY_SCOPE_TEAMS_LIST_FRAGMENT) {
        single { KOIN_KEY_SCOPE_TEAMS_LIST_FRAGMENT }
    }
//    scope(KOIN_KEY_SCOPE_TEAM_NEWS_FRAGMENT) {
//        single { KOIN_KEY_SCOPE_TEAM_NEWS_FRAGMENT }
//    }
//    scope(KOIN_KEY_SCOPE_TEAM_STAFF_FRAGMENT) {
//        single { KOIN_KEY_SCOPE_TEAM_STAFF_FRAGMENT }
//    }
//    scope(KOIN_KEY_SCOPE_TEAM_MATCHES_FRAGMENT) {
//        single { KOIN_KEY_SCOPE_TEAM_MATCHES_FRAGMENT }
//    }
//    scope(KOIN_KEY_SCOPE_TEAM_RESULTS_FRAGMENT) {
//        single { KOIN_KEY_SCOPE_TEAM_RESULTS_FRAGMENT }
//    }
}

enum class ScopeName {
    KOIN_KEY_SCOPE_NEWS_ACTIVITY,

    KOIN_KEY_SCOPE_NEWS_LIST_FRAGMENT,
    KOIN_KEY_SCOPE_NEWS_DETAILS_FRAGMENT,
    KOIN_KEY_SCOPE_MATCHES_FRAGMENT,
    KOIN_KEY_SCOPE_TEAMS_LIST_FRAGMENT,
    KOIN_KEY_SCOPE_TEAM_DETAILS_FRAGMENT,
    KOIN_KEY_SCOPE_TEAM_NEWS_FRAGMENT,
    KOIN_KEY_SCOPE_TEAM_STAFF_FRAGMENT,
    KOIN_KEY_SCOPE_TEAM_MATCHES_FRAGMENT,
    KOIN_KEY_SCOPE_MATCH_DETAILS_FRAGMENT,
    KOIN_KEY_SCOPE_MATCH_NEWS_FRAGMENT,
    KOIN_KEY_SCOPE_MATCH_STATISTICS_FRAGMENT,
    KOIN_KEY_SCOPE_MATCH_STAFF_FRAGMENT,
    KOIN_KEY_SCOPE_MATCH_BROADCAST_FRAGMENT,
    KOIN_KEY_SCOPE_TEAM_RESULTS_FRAGMENT,
    KOIN_KEY_SCOPE_AUTH_FRAGMENT,
    KOIN_KEY_SCOPE_REGISTRATION_FRAGMENT,
    KOIN_KEY_SCOPE_PROFILE_FRAGMENT,
    KOIN_KEY_SCOPE_NEW_COMMENT_FRAGMENT,
    KOIN_KEY_SCOPE_RESET_PASSWORD_FRAGMENT
}

val KOIN_KEY_SCOPE_NEWS_ACTIVITY = ScopeName.KOIN_KEY_SCOPE_NEWS_ACTIVITY.name
val KOIN_KEY_SCOPE_NEWS_LIST_FRAGMENT = ScopeName.KOIN_KEY_SCOPE_NEWS_LIST_FRAGMENT.name
val KOIN_KEY_SCOPE_NEW_DETAILS_FRAGMENT = ScopeName.KOIN_KEY_SCOPE_NEWS_DETAILS_FRAGMENT.name
val KOIN_KEY_SCOPE_MATCHES_FRAGMENT = ScopeName.KOIN_KEY_SCOPE_MATCHES_FRAGMENT.name
val KOIN_KEY_SCOPE_TEAMS_LIST_FRAGMENT = ScopeName.KOIN_KEY_SCOPE_TEAMS_LIST_FRAGMENT.name
val KOIN_KEY_SCOPE_TEAM_DETAILS_FRAGMENT = ScopeName.KOIN_KEY_SCOPE_TEAM_DETAILS_FRAGMENT.name
val KOIN_KEY_SCOPE_TEAM_NEWS_FRAGMENT = ScopeName.KOIN_KEY_SCOPE_TEAM_NEWS_FRAGMENT.name
val KOIN_KEY_SCOPE_TEAM_STAFF_FRAGMENT = ScopeName.KOIN_KEY_SCOPE_TEAM_STAFF_FRAGMENT.name
val KOIN_KEY_SCOPE_TEAM_MATCHES_FRAGMENT = ScopeName.KOIN_KEY_SCOPE_TEAM_MATCHES_FRAGMENT.name
val KOIN_KEY_SCOPE_MATCH_DETAILS_FRAGMENT = ScopeName.KOIN_KEY_SCOPE_MATCH_DETAILS_FRAGMENT.name
val KOIN_KEY_SCOPE_MATCH_NEWS_FRAGMENT = ScopeName.KOIN_KEY_SCOPE_MATCH_NEWS_FRAGMENT.name
val KOIN_KEY_SCOPE_MATCH_STATISTICS_FRAGMENT = ScopeName.KOIN_KEY_SCOPE_MATCH_STATISTICS_FRAGMENT.name
val KOIN_KEY_SCOPE_MATCH_STAFF_FRAGMENT = ScopeName.KOIN_KEY_SCOPE_MATCH_STAFF_FRAGMENT.name
val KOIN_KEY_SCOPE_MATCH_BROADCAST_FRAGMENT = ScopeName.KOIN_KEY_SCOPE_MATCH_BROADCAST_FRAGMENT.name
val KOIN_KEY_SCOPE_TEAM_RESULTS_FRAGMENT = ScopeName.KOIN_KEY_SCOPE_TEAM_RESULTS_FRAGMENT.name
val KOIN_KEY_SCOPE_AUTH_FRAGMENT = ScopeName.KOIN_KEY_SCOPE_AUTH_FRAGMENT.name
val KOIN_KEY_SCOPE_REGISTRATION_FRAGMENT = ScopeName.KOIN_KEY_SCOPE_REGISTRATION_FRAGMENT.name
val KOIN_KEY_SCOPE_PROFILE_FRAGMENT = ScopeName.KOIN_KEY_SCOPE_PROFILE_FRAGMENT.name
val KOIN_KEY_SCOPE_NEW_COMMENT_FRAGMENT = ScopeName.KOIN_KEY_SCOPE_NEW_COMMENT_FRAGMENT.name
val KOIN_KEY_SCOPE_RESET_PASSWORD_FRAGMENT = ScopeName.KOIN_KEY_SCOPE_RESET_PASSWORD_FRAGMENT.name

val KOIN_NAME_REPOSITORY_NEWS = NewsRepository::class.java.name

val KOIN_NAME_DS_NEWS_REMOTE = RemoteNewsDS::class.java.name