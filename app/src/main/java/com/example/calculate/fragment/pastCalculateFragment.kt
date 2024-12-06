package com.example.calculate.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.findFragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calculate.R
import com.example.calculate.adapter.calculateAdapter
import com.example.calculate.databinding.FragmentPastCalculateBinding
import com.example.calculate.model.calculate

@RequiresApi(Build.VERSION_CODES.O)
class pastCalculateFragment : Fragment(R.layout.fragment_past_calculate) {

    private var pastfragment: FragmentPastCalculateBinding? = null
    private val binding get() = pastfragment!!

    private lateinit var  calculateAdapter: calculateAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pastfragment = FragmentPastCalculateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.calculate.setOnClickListener {
            it.findNavController().navigate(R.id.action_pastCalculateFragment_to_HomeFragment, null,
            NavOptions.Builder()
                .setPopUpTo(R.id.pastCalculateFragment, true)
                .build())
        }

        initRecycler()
    }

    private fun initRecycler() {
        calculateAdapter = calculateAdapter()

        binding.recyclerview.apply {
            val adapter = calculateAdapter
            binding.recyclerview.adapter = adapter
            val manager =LinearLayoutManager(context)

            setHasFixedSize(true)
            manager.stackFromEnd = true

            binding.recyclerview.layoutManager = manager
        }

        calculateAdapter.differ.add(calculate(0, "950,000-200,000", "750,000"))
        calculateAdapter.differ.add(calculate(1, "950,000-300,000", "650,000"))
        calculateAdapter.differ.add(calculate(2, "40+39", "79"))
        calculateAdapter.differ.add(calculate(3, "6×8", "48"))
        calculateAdapter.differ.add(calculate(4, "50÷5", "10"))
    }
}