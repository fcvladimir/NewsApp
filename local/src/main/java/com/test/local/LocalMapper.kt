package com.test.local

import com.test.data.model.NewEntity
import com.test.data.model.SourceEntity
import com.test.local.models.NewRealm
import com.test.local.models.SourceRealm

class LocalMapper {

    fun fromDomainNews(input: List<NewRealm>): List<NewEntity> {
        return input
                .map {
                    NewEntity(title = it.title,
                            publishedAt = it.publishedAt)
                }
    }

    fun fromDomainNew(input: NewEntity): NewRealm {
        val newRealm = NewRealm()
        newRealm.title = input.title
        newRealm.source = fromDomainNewSource(input.source)
        newRealm.publishedAt = input.publishedAt
        return newRealm
    }

    fun fromDomainNewSource(input: SourceEntity?): SourceRealm {
        val sourceRealm = SourceRealm()
        sourceRealm.id = input?.id
        sourceRealm.name = input?.name
        return sourceRealm
    }
}