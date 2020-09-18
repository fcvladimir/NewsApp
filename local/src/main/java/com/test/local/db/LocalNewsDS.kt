package com.test.local.db

import com.test.data.datasources.INewsDS
import com.test.data.model.NewEntity
import com.test.local.LocalMapper
import com.test.local.models.NewRealm
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import io.realm.Realm

class LocalNewsDS(
        private val mapper: LocalMapper
) : INewsDS {

    override fun dispatchNewToFavorites(newEntity: NewEntity): Completable {
        return Completable.create {
            Realm.getDefaultInstance().executeTransaction { realm ->
                val new = mapper.fromDomainNew(newEntity)
                realm.copyToRealmOrUpdate(new)
            }
        }
                .subscribeOn(Schedulers.io())
    }

    override fun fetchFavoriteNews(): Single<List<NewEntity>> {
        return Single.create<List<NewEntity>> { e ->
            Realm.getDefaultInstance().executeTransaction { realm ->
                val news = realm.where(NewRealm::class.java).findAll()
                if (news.isEmpty()) {
                    e.onError(NoSuchElementException())
                    return@executeTransaction
                }
                e.onSuccess(mapper.fromDomainNews(news))
            }
        }
                .subscribeOn(Schedulers.io())
    }

}