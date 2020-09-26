package com.example.trackertest.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.example.trackertest.R
import com.example.trackertest.databinding.FragmentRealtimeBinding
import com.example.trackertest.ui.MainActivity
import com.example.trackertest.ui.MainViewModel

class RealTimeFragment : Fragment() {

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var binding : FragmentRealtimeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_realtime, container, false)

        val view = binding.root

        binding.lifecycleOwner = viewLifecycleOwner

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mainViewModel.record.observe(viewLifecycleOwner, Observer {
            binding.tvDate.text = if(it.createdAt.isEmpty()) "None record received" else it.createdAt
            binding.tvLatLong.text =  "${it.latitude} / ${it.longitude}"
            binding.tvSpeed.text =  "${it.speed}"
            binding.tvAcc.text =  "${it.accuracy}"
        })

        mainViewModel.lastSpeedMin.observe(viewLifecycleOwner, Observer {
            binding.tvSpeedMin.text =  it.toString()
        })

        mainViewModel.lastSpeedMax.observe(viewLifecycleOwner, Observer {
            binding.tvSpeedMax.text =  it.toString()

        })

        mainViewModel.lastSpeedAvg.observe(viewLifecycleOwner, Observer {
           binding.tSpeedAvg.text = it.toString()
        })
    }
}