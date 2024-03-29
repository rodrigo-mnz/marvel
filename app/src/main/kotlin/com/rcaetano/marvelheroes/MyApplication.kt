package com.rcaetano.marvelheroes

import android.app.Application
import com.rcaetano.marvelheroes.di.appModule
import com.rcaetano.marvelheroes.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(
                listOf(
                    appModule,
                    networkModule
                )
            )
        }
    }
}
