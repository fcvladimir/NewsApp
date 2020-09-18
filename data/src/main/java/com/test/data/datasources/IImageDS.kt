package com.test.data.datasources

import io.reactivex.Single

interface IImageDS {

    fun saveImage(imageUrl: String): Single<String> {
        return Single.error(UnsupportedOperationException("This DS implementation does not support saveImage() function"))
    }
}