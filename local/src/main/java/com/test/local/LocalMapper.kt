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
                            SourceEntity(it.source?.id,
                                    it.source?.name),
                            publishedAt = it.publishedAt,
                            it.urlToImage)
                }
    }

    fun fromDomainNew(input: NewEntity): NewRealm {
        val newRealm = NewRealm()
        newRealm.title = input.title
        newRealm.source = fromDomainNewSource(input.source)
        newRealm.publishedAt = input.publishedAt
        newRealm.urlToImage = input.urlToImage
        return newRealm
    }

    fun fromDomainNewSource(input: SourceEntity?): SourceRealm {
        val sourceRealm = SourceRealm()
        sourceRealm.id = input?.id
        sourceRealm.name = input?.name
        return sourceRealm
    }
}