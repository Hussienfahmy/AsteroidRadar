package com.udacity.asteroidradar.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.api.NeoWsRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import retrofit2.HttpException

@HiltWorker
class RefreshAsteroidsWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val repository: NeoWsRepository,
): CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = try {
        repository.dailyRefreshAsteroidsData()
        Result.success()
    }catch (e: HttpException) {
        Result.retry()
    }

    companion object {
        const val WORK_NAME = "refresh_asteroid"
    }
}