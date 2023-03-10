package com.skoove.challenge.data.di

import com.skoove.challenge.data.BuildConfig
import com.skoove.challenge.data.Repository
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Modules {

    val common = module {
        single { createJson() }
        single { createHttpClient() }
        single { createRetrofit(json = get(), okHttpClient = get()) }
        single { Repository(retrofit = get()) }
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun createJson(): Json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
        coerceInputValues = true
    }

    @OptIn(ExperimentalSerializationApi::class)
    fun createRetrofit(json: Json, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().client(okHttpClient).baseUrl(BuildConfig.CONNECTION_URL)
//            .addConverterFactory(json.asConverterFactory("application/json".toMediaType())).build()
            .addConverterFactory(GsonConverterFactory.create()).build()
    }

    @Suppress("KotlinConstantConditions")
    fun createHttpClient(): OkHttpClient = OkHttpClient.Builder().apply {
        if (BuildConfig.BUILD_TYPE != "release") {
            addInterceptor(HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            })
        }
    }.build()
}
