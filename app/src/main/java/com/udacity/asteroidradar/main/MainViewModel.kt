package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
    private val workManager: WorkManager,
) : ViewModel() {

    val asteroids = repository.asteroids

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay> = _pictureOfDay

    init {
        viewModelScope.launch {
            // get the picture of the day
            _pictureOfDay.value = repository.getPicOTheDay().getOrNull()

            // refresh when user open the app
            workManager.enqueue(
                OneTimeWorkRequestBuilder<RefreshAsteroidsWorker>().setConstraints(
                    Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
                ).build()
            )
        }
    }
}