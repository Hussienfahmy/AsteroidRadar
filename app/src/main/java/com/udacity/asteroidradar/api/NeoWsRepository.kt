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

    val asteroids = dao.asteroids

    /**
     * Deletes all the data in the cache then save an updated one
     */
    suspend fun refreshAsteroidsData() {
        val calendar = Calendar.getInstance()
        val currentTime = calendar.time
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val todayFormatted = dateFormat.format(currentTime)

        withContext(ioDispatcher) {
            dao.deleteAllPast(todayFormatted)
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