package com.example.stopwatch.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.*
import com.example.stopwatch.R
import com.example.stopwatch.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding // Binding object for activity_main.xml layout
//    private lateinit var stopwatch : Chronometer // holds reference to Chronometer
    private var running = false // flag for stopwatch running/stop
    var offset : Long = 0 // will hold the correct time in case of pause, reset.
//    lateinit var tvStopwatchNickname : TextView
    //keys for Bundle
    val OFFSET_KEY = "offset"
    val BASE_KEY = "base"
    val RUNNING_KEY = "running"
    val NICKNAME_KEY = "nickname"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater) // links activity_main.xml to binding object
        val rootView = binding.root
        setContentView(rootView)

        // get ref to stopwatch
//        stopwatch = findViewById(R.id.stopwatch)
//        tvStopwatchNickname = findViewById(R.id.tv_stopwatch_nickname)
//        tvStopwatchNickname = binding.tvStopwatchNickname
        if(savedInstanceState != null) {

            binding.tvStopwatchNickname.text = savedInstanceState.getString(NICKNAME_KEY)
            // the activity is being re-instantiated, and there must be a saved bundle
            offset = savedInstanceState.getLong(OFFSET_KEY)
            running = savedInstanceState.getBoolean(RUNNING_KEY)
            if(running) {
                // if it has been running, you want it to run
                binding.stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                binding.stopwatch.start()
            } else {
                setBaseTime()
            }
        }

        // start button starts the watch if it's not running
//        val startButton = findViewById<Button>(R.id.startBTN)
        binding.startBTN.setOnClickListener {
            if(!running) {
                setBaseTime() // to set the base property of chronometer
                binding.stopwatch.start() // will start from whatever base-time you set
                running = true
            }
        }

        // pause button will pause the stopwatch
//        val pauseButton = findViewById<Button>(R.id.pauseBTN)
        binding.pauseBTN.setOnClickListener {
            saveOffset() // whenever interrupted save time as offset
            binding.stopwatch.stop()
            running = false
        }

        // to reset the timer
//        val resetButton = findViewById<Button>(R.id.resetBTN)
        binding.resetBTN.setOnClickListener {
            offset = 0
            setBaseTime()
        }

//        val stopButton = findViewById<Button>(R.id.stopBTN)
        binding.stopBTN.setOnClickListener {
            offset = 0
            running = false
            setBaseTime()
            binding.stopwatch.stop()
        }

//        val generateNicknameButton = findViewById<Button>(R.id.bt_generate_nickname)
        binding.btGenerateNickname.setOnClickListener {
            val etNickname = findViewById<EditText>(R.id.et_nickname)
            val etNicknameText = etNickname.text.toString()
            if(etNicknameText.isNotEmpty()) {
                binding.tvStopwatchNickname.text = etNicknameText
            }
        }

    }

    // called right before onDestroy()
    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putString(NICKNAME_KEY, binding.tvStopwatchNickname.text.toString())
        savedInstanceState.putBoolean(RUNNING_KEY, running)    // a bundle takes input as key-value pair
        savedInstanceState.putLong(OFFSET_KEY, offset)
        savedInstanceState.putLong(BASE_KEY, binding.stopwatch.base)
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        // stopwatch should pause(if was running before)
        if(running) {
            saveOffset()
            binding.stopwatch.stop()
        }
    }

    override fun onResume() {
        super.onResume()
        // stopwatch should start(if was running before) from where it was paused
        if(running) {
            setBaseTime()
            offset = 0
            binding.stopwatch.start()
        }
    }

    private fun setBaseTime() {
        binding.stopwatch.base = SystemClock.elapsedRealtime() - offset
    }

    private fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - binding.stopwatch.base
    }
}