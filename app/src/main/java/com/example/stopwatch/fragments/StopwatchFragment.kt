package com.example.stopwatch.fragments

import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import com.example.stopwatch.databinding.FragmentStopwatchBinding

class StopwatchFragment : Fragment() {
    private var _binding : FragmentStopwatchBinding ?= null
    private val binding get() = _binding!!

    private var running : Boolean = false
    private var offset : Long = 0

    // keys for savedInstanceState bundle
    val OFFSET_KEY = "offsetKey"
    val BASE_KEY = "baseKey"
    val RUNNING_KEY = "runningKey"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentStopwatchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        if(savedInstanceState != null) {

            running = savedInstanceState.getBoolean(RUNNING_KEY)

            if(running) {
                binding.stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                binding.stopwatch.start()
            } else {
                offset = savedInstanceState.getLong(OFFSET_KEY)
                setBaseTime()
            }
        }

        // setting up nickname tv
        binding.tvStopwatchNickname.text = StopwatchFragmentArgs.fromBundle(requireArguments()).stopwatchNickname

        // setting up startButton
        binding.startBTN.setOnClickListener {
            if(!running) {
                setBaseTime()
                binding.stopwatch.start()
                running = true
            }
        }

        // setting up pauseButton
        binding.pauseBTN.setOnClickListener {
            if(running) {
                saveOffset()
                binding.stopwatch.stop()
                running = false
            }
        }

        // setting up resetButton
        binding.resetBTN.setOnClickListener {
            offset = 0
            setBaseTime()
        }

        // setting up stopButton
        binding.stopBTN.setOnClickListener {
            offset = 0
            setBaseTime()
            binding.stopwatch.stop()
            running = false
        }

        super.onViewCreated(view, savedInstanceState)
    }


    // fragment out of focus
    override fun onPause() {
        super.onPause()
        if(running) {
            saveOffset()
            binding.stopwatch.stop()
        }
    }

    // running(true) -> pause it in onPause() -> running(true) -> start it in onResume()
    // running(false) -> do nothing in onPause() -> running(false) -> do nothing in onResume()

    // fragment back in focus
    override fun onResume() {
        super.onResume()
        if(running) {
            setBaseTime()
            binding.stopwatch.start()
        }
    }

    // this is called when the host activity calls its own onSavedInstanceState()
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(BASE_KEY, binding.stopwatch.base)
        outState.putBoolean(RUNNING_KEY, running)
        outState.putLong(OFFSET_KEY, offset)
    }
    private fun setBaseTime() {
        binding.stopwatch.base = SystemClock.elapsedRealtime() - offset
    }

    private fun saveOffset() {
        offset = SystemClock.elapsedRealtime() - binding.stopwatch.base
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}