package com.udacity.asteroidradar.api

import android.icu.util.Calendar
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.database.AsteroidDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NeoWsRepository @Inject constructor(
    private val dao: AsteroidDao,
    private val ioDispatcher: CoroutineDispatcher,
) {

    private val retrofit = NeoWsApi.retrofitService

    val nextWeekAsteroids = dao.nextWeekAsteroids
    val todayAsteroids = dao.todayAsteroids
    val savedAsteroid = dao.savedPastAsteroids

    /**
     * Deletes all the data in the cache then save an updated one
     */
    suspend fun dailyRefreshAsteroidsData() {
        withContext(ioDispatcher) {
            dao.deleteAllPast()
            getNextWeekAsteroidsData()
        }
    }

    /**
     * get the next 7 days and store them
     */
    suspend fun getNextWeekAsteroidsData() {
        withContext(ioDispatcher) {
            val asteroidsString = retrofit.getAsteroidsAsync().await()
            val asteroids = parseAsteroidsJsonResult(JSONObject(asteroidsString))
            dao.insert(asteroids)
        }
    }

    /**
     * safe call to avoid exceptions when the device is offline
     */
    suspend fun getPicOTheDay() = kotlin.runCatching {
        withContext(ioDispatcher) {
            retrofit.getPicOfTheDayAsync().await()
        }
    }
}