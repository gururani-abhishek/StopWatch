package com.example.stopwatch.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.stopwatch.databinding.FragmentStopwatchBinding
import com.example.stopwatch.viewmodels.StopwatchViewModel

class StopwatchFragment : Fragment() {
    private var _binding : FragmentStopwatchBinding ?= null
    private val binding get() = _binding!!

    private lateinit var viewModel : StopwatchViewModel

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
        super.onViewCreated(view, savedInstanceState)


        viewModel = ViewModelProvider(this)[StopwatchViewModel::class.java]


        if(savedInstanceState != null) {

            viewModel.running = savedInstanceState.getBoolean(RUNNING_KEY)

            if(viewModel.running) {
                binding.stopwatch.base = savedInstanceState.getLong(BASE_KEY)
                binding.stopwatch.start()
            } else {
                viewModel.offset = savedInstanceState.getLong(OFFSET_KEY)
                viewModel.setBaseTime()
            }
        }

        // adding an obsever to stopwatchState
        viewModel.stopwatchState.observe(viewLifecycleOwner, Observer {liveStopwatchState ->
            if(liveStopwatchState) {
                binding.stopwatch.start()
            } else {
                binding.stopwatch.stop()
            }
        })

        viewModel.baseTime.observe(viewLifecycleOwner, Observer {liveBaseTime ->
            binding.stopwatch.base = liveBaseTime
        })

        // setting up nickname tv
        binding.tvStopwatchNickname.text = StopwatchFragmentArgs.fromBundle(requireArguments()).stopwatchNickname

        // setting up startButton
        binding.startBTN.setOnClickListener {
            viewModel.onStartButtonPressed()
        }

        // setting up pauseButton
        binding.pauseBTN.setOnClickListener {
            viewModel.onPauseButtonPressed()
        }

        // setting up resetButton
        binding.resetBTN.setOnClickListener {
            viewModel.onResetButtonPressed()
        }

        // setting up stopButton
        binding.stopBTN.setOnClickListener {
            viewModel.onStopButtonPressed()
        }
    }


//     fragment out of focus
    override fun onPause() {
        super.onPause()
        viewModel.onFocussed()
    }

//     running(true) -> pause it in onPause() -> running(true) -> start it in onResume()
//     running(false) -> do nothing in onPause() -> running(false) -> do nothing in onResume()
//
//     fragment back in focus
    override fun onResume() {
        super.onResume()
        viewModel.onUnfocused()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong(BASE_KEY, binding.stopwatch.base)
        outState.putBoolean(RUNNING_KEY, viewModel.running)
        outState.putLong(OFFSET_KEY, viewModel.offset)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}