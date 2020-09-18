package com.test.data.mapper

import com.test.data.model.NewEntity
import com.test.data.model.SourceEntity
import com.test.domain.model.New
import com.test.domain.model.Source

class DataMapper {

    fun mapFromNewEntity(input: List<NewEntity>): List<New> {
        return input.map {
            toDomainNew(it)
        }
    }

    fun toDomainNew(input: NewEntity): New {
        return New(input.title,
                Source(input.source?.id,
                        input.source?.name),
                input.publishedAt,
                input.urlToImage)
    }

    fun mapToNewEntity(input: New): NewEntity {
        return NewEntity(input.title,
                SourceEntity(input.source?.id,
                        input.source?.name),
                input.publishedAt,
                input.urlToImage)
    }
}