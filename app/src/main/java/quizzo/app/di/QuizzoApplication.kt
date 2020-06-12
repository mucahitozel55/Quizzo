package quizzo.app.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class QuizzoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@QuizzoApplication)
            modules(listOf(databaseModule, networkModule, repositoryModule, viewModelModule))
        }
    }
}