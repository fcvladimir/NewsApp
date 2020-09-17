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
                val newCustomers = mapper.fromDomainNew(newEntity)
                realm.copyToRealmOrUpdate(newCustomers)
            }
        }
                .subscribeOn(Schedulers.io())
    }

    override fun fetchFavoriteNews(): Single<List<NewEntity>> {
        return Single.create<List<NewEntity>> { e ->
            Realm.getDefaultInstance().executeTransaction { realm ->
                val customers = realm.where(NewRealm::class.java).findAll()
                if (customers.isEmpty()) {
                    e.onError(NoSuchElementException())
                    return@executeTransaction
                }
                e.onSuccess(mapper.fromDomainNews(customers))
            }
        }
                .subscribeOn(Schedulers.io())
    }

}