package com.test.remote.mapper

import android.content.res.Resources
import com.test.data.model.NewEntity
import com.test.data.model.SourceEntity
import com.test.remote.model.NewsJson

class RemoteMapper {

    fun mapFromNewsRemote(input: List<NewsJson>?): List<NewEntity> {
        if (input == null) throw Resources.NotFoundException()
        return input.map {
            toDomainNew(it)
        }
    }

    fun toDomainNew(input: NewsJson): NewEntity {
        return NewEntity(input.title,
                SourceEntity(input.source?.id,
                        input.source?.name),
                input.publishedAt,
                input.urlToImage)
    }
}