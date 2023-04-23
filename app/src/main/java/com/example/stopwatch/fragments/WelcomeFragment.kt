package com.example.stopwatch.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.stopwatch.R
import com.example.stopwatch.databinding.FragmentWelcomeBinding

class WelcomeFragment : Fragment() {
    private var _binding : FragmentWelcomeBinding ?= null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)

        binding.btnStopwatch.setOnClickListener {
            val etNickname = binding.etNickname.text
            if(etNickname.isNullOrEmpty()) {
                Toast.makeText(activity, "give your stopwatch a nickname", Toast.LENGTH_SHORT).show()
            } else {
                val stopwatchNickname = etNickname.toString()
                val action = WelcomeFragmentDirections.actionWelcomeFragmentToStopwatchFragment(stopwatchNickname)
                val navController = findNavController()
                navController.navigate(action)
            }

            etNickname.clear()
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}