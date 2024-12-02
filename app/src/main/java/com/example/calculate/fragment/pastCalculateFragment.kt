package com.example.calculate.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.calculate.R
import com.example.calculate.databinding.FragmentHomeBinding
import com.example.calculate.databinding.FragmentPastCalculateBinding

@RequiresApi(Build.VERSION_CODES.O)
class pastCalculateFragment : Fragment(R.layout.fragment_past_calculate) {

    private var pastfragment: FragmentPastCalculateBinding? = null
    private val binding get() = pastfragment!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pastfragment = FragmentPastCalculateBinding.inflate(inflater, container, false)
        return binding.root
    }
}