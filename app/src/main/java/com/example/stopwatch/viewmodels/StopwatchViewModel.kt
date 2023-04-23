package com.example.stopwatch.viewmodels

import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StopwatchViewModel : ViewModel() {
    private val _stopwatchState = MutableLiveData(false) // a flag -> true(stopwatch start), false(stopwatch stop)
    val stopwatchState : LiveData<Boolean> get() = _stopwatchState

    private val _baseTime = MutableLiveData<Long>(SystemClock.elapsedRealtime())
    val baseTime : LiveData<Long> get() = _baseTime

    var running : Boolean = false
    var offset : Long = 0


    fun onStartButtonPressed() {
        if(!running) {
            setBaseTime()
            _stopwatchState.value = true
            running = true
        }
    }

    fun onStopButtonPressed() {
        offset = 0
        setBaseTime()
        _stopwatchState.value = false
        running = false
    }

    fun onPauseButtonPressed() {
        if(running) {
            saveOffset()
            _stopwatchState.value = false
            running = false
        }
    }

    fun onResetButtonPressed() {
        offset = 0
        setBaseTime()
    }

    fun setBaseTime() {
        _baseTime.value = SystemClock.elapsedRealtime() - offset
    }

    fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - (_baseTime.value!!)
    }

    fun onUnfocused() {
        if(running) {
            setBaseTime()
            _stopwatchState.value = true
        }
    }

    fun onFocussed() {
        if(running) {
            saveOffset()
            _stopwatchState.value = false
        }
    }
}