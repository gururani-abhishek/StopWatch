package com.example.stopwatch.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WelcomeViewModel : ViewModel() {

    private var _nicknameGiven = MutableLiveData<Boolean>(false)
    val nicknameGiven : LiveData<Boolean> get() = _nicknameGiven

    var nickname : String = "stopwatch"
    fun giveNickname(nickname : String) {
        this.nickname = nickname
        _nicknameGiven.value = true
    }

    fun fetchNickname() : String {
        return nickname
    }
}