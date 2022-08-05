package com.udacity.asteroidradar.main

import androidx.lifecycle.*
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.NeoWsRepository
import com.udacity.asteroidradar.work.RefreshAsteroidsWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: NeoWsRepository,
) : ViewModel() {

    private val _filterAsteroids = MutableLiveData<AsteroidFilter>()
        .apply {
            value = AsteroidFilter.WEEK
        }

    val asteroids = _filterAsteroids.map {
        when(it ?: AsteroidFilter.WEEK) {
            AsteroidFilter.TODAY -> repository.todayAsteroids
            AsteroidFilter.WEEK -> repository.nextWeekAsteroids
            AsteroidFilter.SAVED -> repository.savedAsteroid
        }
    }

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay> = _pictureOfDay

    init {
        viewModelScope.launch {
            // get the picture of the day
            _pictureOfDay.value = repository.getPicOTheDay().getOrNull()

            // when user open the app fetch next week data
            repository.getNextWeekAsteroidsData()
        }
    }

    fun setFilter(filter: AsteroidFilter) {
        _filterAsteroids.value = filter
    }
}

enum class AsteroidFilter {
    TODAY, WEEK, SAVED
}
