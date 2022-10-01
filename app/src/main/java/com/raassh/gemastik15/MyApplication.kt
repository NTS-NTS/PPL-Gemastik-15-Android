package com.raassh.gemastik15

import android.app.Application
import com.raassh.gemastik15.di.networkModule
import com.raassh.gemastik15.di.repositoryModule
import com.raassh.gemastik15.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.NONE)
            androidContext(this@MyApplication)
            modules(listOf(networkModule, repositoryModule, viewModelModule))
        }
    }
}