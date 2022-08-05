package com.udacity.asteroidradar

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.*
import com.udacity.asteroidradar.work.RefreshAsteroidsWorker
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class AsteroidRadarApplication: Application(), Configuration.Provider {

    private val appScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        // schedule periodic to refresh the cache in the background
        scheduleRefreshWorkManager()
    }

    private fun scheduleRefreshWorkManager() {
        appScope.launch {
            val constraints = Constraints.Builder()
                .setRequiresCharging(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val periodicRequest = PeriodicWorkRequestBuilder<RefreshAsteroidsWorker>(
                1,
                TimeUnit.DAYS
            ).setConstraints(constraints).build()

            WorkManager.getInstance(this@AsteroidRadarApplication)
                .enqueueUniquePeriodicWork(
                    RefreshAsteroidsWorker.WORK_NAME,
                    ExistingPeriodicWorkPolicy.KEEP,
                    periodicRequest
                )
        }
    }

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}