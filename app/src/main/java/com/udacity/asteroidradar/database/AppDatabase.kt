package com.udacity.asteroidradar.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.udacity.asteroidradar.Asteroid

@Database(entities = [Asteroid::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val dao: AsteroidDao

}