package com.test.remote.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*

data class NewsJson(
        @SerializedName("title")
        @Expose
        val title: String? = null,
        @SerializedName("source")
        @Expose
        val source: SourceJson? = null,
        @SerializedName("publishedAt")
        @Expose
        val publishedAt: Date? = null
)

data class SourceJson(
        @SerializedName("id")
        @Expose
        val id: String? = null,
        @SerializedName("name")
        @Expose
        val name: String? = null
)