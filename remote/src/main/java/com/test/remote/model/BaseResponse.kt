package com.test.remote.model

import com.google.gson.annotations.SerializedName

data class BaseResponse<T>(@SerializedName("status") var status: String,
                           @SerializedName("error") var error: String?,
                           @SerializedName("articles") var data: T?)
