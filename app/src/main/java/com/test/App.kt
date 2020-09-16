package com.test

import android.app.Application
import com.test.di.mainModule
import org.koin.android.ext.android.startKoin
import org.koin.android.logger.AndroidLogger
import org.koin.log.EmptyLogger

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(
                this@App,
                listOf(mainModule),
                logger =
                if (BuildConfig.DEBUG) {
                    AndroidLogger()
                } else {
                    EmptyLogger()
                }
        )
    }
}