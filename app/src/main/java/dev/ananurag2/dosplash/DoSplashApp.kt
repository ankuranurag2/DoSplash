package dev.ananurag2.dosplash

import android.app.Application
import dev.ananurag2.dosplash.di.listModule
import dev.ananurag2.dosplash.di.networkModule
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * created by ankur on 21/10/20
 */
class DoSplashApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.ERROR)
            modules(listOf(networkModule, listModule))
        }
    }
}