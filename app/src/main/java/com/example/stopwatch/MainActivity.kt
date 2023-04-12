package com.example.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer

class MainActivity : AppCompatActivity() {

    private lateinit var stopwatch : Chronometer // holds reference to Chronometer
    private var running = false // flag for stopwatch running/stop
    var offset : Long = 0 // will hold the correct time in case of pause, reset.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // get ref to stopwatch
        stopwatch = findViewById(R.id.stopwatch)

        // start button starts the watch if it's not running
        val startButton = findViewById<Button>(R.id.startBTN)
        startButton.setOnClickListener {
            if(!running) {
                setBaseTime() // to set the base property of chronometer
                stopwatch.start() // will start from whatever base-time you set
                running = true
            }
        }

        // pause button will pause the stopwatch
        val pauseButton = findViewById<Button>(R.id.pauseBTN)
        pauseButton.setOnClickListener {
            saveOffset() // whenever interrupted save time as offset
            stopwatch.stop()
            running = false
        }

        // to reset the timer
        val resetButton = findViewById<Button>(R.id.resetBTN)
        resetButton.setOnClickListener {
            offset = 0
            setBaseTime()
        }

        val stopButton = findViewById<Button>(R.id.stopBTN)
        stopButton.setOnClickListener {
            offset = 0
            running = false
            setBaseTime()
            stopwatch.stop()
        }
    }

    private fun setBaseTime() {
        stopwatch.base = SystemClock.elapsedRealtime() - offset
    }

    private fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - stopwatch.base
    }
}