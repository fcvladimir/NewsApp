package com.test.data.model

import java.util.*

data class NewEntity(
        val title: String? = null,
        val source: SourceEntity? = null,
        val publishedAt: Date? = null
)

data class SourceEntity(
        val id: String? = null,
        val name: String? = null
)