package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(asteroids: List<Asteroid>)

    @get:Query("SELECT * FROM asteroid ORDER BY closeApproachDate ASC")
    val asteroids: LiveData<List<Asteroid>>

    @Query("DELETE FROM asteroid WHERE closeApproachDate < :today")
    suspend fun deleteAllPast(today: String)

}
