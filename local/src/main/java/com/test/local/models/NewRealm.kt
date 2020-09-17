package com.test.local.models

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.util.*

open class NewRealm : RealmObject() {
    @PrimaryKey
    var title: String? = null
    var source: SourceRealm? = null
    var publishedAt: Date? = null
}

open class SourceRealm : RealmObject() {
    var id: String? = null
    var name: String? = null
}