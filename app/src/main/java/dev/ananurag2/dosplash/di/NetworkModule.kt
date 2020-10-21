package dev.ananurag2.dosplash.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dev.ananurag2.dosplash.BuildConfig
import dev.ananurag2.dosplash.data.network.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

const val BASE_URL = "https://api.unsplash.com/"


const val DEFAULT_TIME_OUT = 60L

val networkModule = module {
    factory { getLoggingInterceptor() }
    factory { getOkHttpClient(get()) }
    factory { getApiService(get()) }
    factory { getMoshi() }

    single { getRetrofit(get(), get()) }
}

fun getOkHttpClient(interceptor: HttpLoggingInterceptor): OkHttpClient {
    val builder = OkHttpClient().newBuilder()

    if (BuildConfig.DEBUG)
        builder.addInterceptor(interceptor)

    return builder.connectTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
        .writeTimeout(DEFAULT_TIME_OUT, TimeUnit.SECONDS)
        .build()
}

fun getLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}


fun getRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
    return Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .client(okHttpClient)
        .build()
}

fun getMoshi(): Moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

fun getApiService(retrofit: Retrofit): ApiService =
    retrofit.create(ApiService::class.java)