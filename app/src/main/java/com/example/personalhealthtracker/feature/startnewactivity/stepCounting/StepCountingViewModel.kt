package com.example.personalhealthtracker.feature.startnewactivity.stepCounting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StepCountingViewModel : ViewModel() {

    private val _stepCount = MutableLiveData<Int>()
    val stepCount: LiveData<Int> = _stepCount

    private val _heartRate = MutableLiveData<Int>()
    val heartRate: LiveData<Int> = _heartRate

    private val _distance = MutableLiveData<Float>()
    val distance: LiveData<Float> = _distance

    private val _time = MutableLiveData<Int>()
    val time: LiveData<Int> = _time

    private val _isTracking = MutableLiveData<Boolean>()
    val isTracking: LiveData<Boolean> = _isTracking

    init {
        _stepCount.value = 0
        _heartRate.value = 70
        _distance.value = 0f
        _time.value = 0
        _isTracking.value = false
    }

    fun updateStepCount(count: Int) {
        _stepCount.postValue(count)
    }

    fun updateHeartRate(rate: Int) {
        _heartRate.postValue(rate)
    }

    fun updateDistance(distance: Float) {
        _distance.postValue(distance)
    }

    fun updateTime(time: Int) {
        _time.postValue(time)
    }

    fun startTracking() {
        _isTracking.postValue(true)
    }

    fun stopTracking() {
        _isTracking.postValue(false)
    }
}
