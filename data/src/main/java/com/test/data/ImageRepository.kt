package com.test.data

import com.test.data.datasources.IImageDS
import com.test.domain.IRepository
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

class ImageRepository(
        private val imageDs: IImageDS
) : IRepository<String> {

    override fun <P : IRepository.Get<String>> get(param: P): Single<String> {
        return when (param) {
            is IRepository.Get.SaveImage -> saveImage(param.imageUrl)
            else -> throw UnsupportedOperationException("${param::class.java.name} operation is unsupported")
        }
    }

    override fun <P : IRepository.Update<String>> update(param: P): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <P : IRepository.Delete> delete(param: P): Completable {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun <P : IRepository.Observe> observe(param: P): Observable<String> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun saveImage(imageUrl: String) =
            imageDs.saveImage(imageUrl)

}