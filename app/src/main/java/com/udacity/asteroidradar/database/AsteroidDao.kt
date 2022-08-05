package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(asteroids: List<Asteroid>)

    // date() function returns today date formatted as YYYY-MM-DD Like in the [Constants.kt]

    @get:Query("""
        SELECT * FROM asteroid
        WHERE closeApproachDate >= date()
        ORDER BY closeApproachDate ASC""")
    val nextWeekAsteroids: LiveData<List<Asteroid>>

    @get:Query("""
        SELECT * FROM asteroid
        WHERE closeApproachDate = date()
        ORDER BY closeApproachDate ASC""")
    val todayAsteroids: LiveData<List<Asteroid>>

    @get:Query("""
        SELECT * FROM asteroid
        WHERE closeApproachDate < date()
        ORDER BY closeApproachDate ASC""")
    val savedPastAsteroids: LiveData<List<Asteroid>>

    @Query("DELETE FROM asteroid WHERE closeApproachDate < datetime()")
    suspend fun deleteAllPast()

}
