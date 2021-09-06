package edu.self.cryptotechnicaltest

import android.app.Application
import edu.self.cryptotechnicaltest.core.di.listOfModule
import kotlinx.coroutines.DelicateCoroutinesApi
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

@DelicateCoroutinesApi
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(listOfModule)
        }
    }
}