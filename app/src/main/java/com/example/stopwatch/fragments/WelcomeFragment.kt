package com.example.stopwatch.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.stopwatch.R
import com.example.stopwatch.databinding.FragmentWelcomeBinding
import com.example.stopwatch.viewmodels.WelcomeViewModel

class WelcomeFragment : Fragment() {
    private var _binding : FragmentWelcomeBinding ?= null
    private val binding get() = _binding!!

    private lateinit var viewModel : WelcomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWelcomeBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[WelcomeViewModel::class.java]

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.nicknameGiven.observe(viewLifecycleOwner, Observer { liveValueNicknameGiven ->
            if(liveValueNicknameGiven) {
                val action = WelcomeFragmentDirections.actionWelcomeFragmentToStopwatchFragment(viewModel.fetchNickname())
                val navController = findNavController()
                navController.navigate(action)
            }
        })

        binding.btnStopwatch.setOnClickListener {
            val etNickname = binding.etNickname.text
            if(etNickname.isNullOrEmpty()) {
                Toast.makeText(activity, "give your stopwatch a nickname", Toast.LENGTH_SHORT).show()
            } else {
                val stopwatchNickname = etNickname.toString()
                viewModel.giveNickname(stopwatchNickname)
            }
            etNickname.clear()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}