package com.test.domain.model

import java.io.Serializable
import java.util.*

data class New(
        val title: String? = null,
        val source: Source? = null,
        val publishedAt: Date? = null
) : Serializable

data class Source(
        val id: String? = null,
        val name: String? = null
) : Serializable