package com.rcaetano.marvelheroes

import androidx.test.espresso.IdlingResource
import com.rcaetano.marvelheroes.data.ApiService
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun createMockNetworkModule(mockServerUrl: String) = module {

    factory {
        OkHttpClient
            .Builder()
            .build()
    }

    factory<Converter.Factory> { GsonConverterFactory.create() }

    single {
        val okHttpClient = get<OkHttpClient>()
        OkHttp3IdlingResource("idle", okHttpClient.dispatcher)

        Retrofit.Builder()
            .baseUrl(mockServerUrl)
            .client(okHttpClient)
            .addConverterFactory(get())
            .build()
            .create(ApiService::class.java)
    }
}

class OkHttp3IdlingResource(
    private val resourceName: String,
    private val dispatcher: Dispatcher
) : IdlingResource {

    @Volatile
    internal var callback: IdlingResource.ResourceCallback? = null

    init {
        dispatcher.idleCallback = Runnable {
            val callback = this@OkHttp3IdlingResource.callback
            callback?.onTransitionToIdle()
        }
    }

    override fun getName() = resourceName
    override fun isIdleNow() = dispatcher.runningCallsCount() == 0

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback) {
        this.callback = callback
    }
}
