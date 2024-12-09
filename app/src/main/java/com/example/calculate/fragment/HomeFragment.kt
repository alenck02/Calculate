package com.example.calculate.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.example.calculate.R
import com.example.calculate.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pastTimeBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_HomeFragment_to_pastCalculateFragment, null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.HomeFragment, true)
                    .build())
        }

        btn_click()
    }

    fun btn_click() {
        binding.buttonC.setOnClickListener {
            binding.expression.text = ""
            binding.answer.text = ""
        }

        binding.button0.setOnClickListener {
            if (binding.expression.text == "0") {
                binding.expression.text = "${binding.expression.text}"
            } else if (binding.expression.text == "") {
                binding.expression.text = "0"
            } else {
                binding.expression.text = "${binding.expression.text}0"
            }
        }

        binding.button1.setOnClickListener {
            binding.expression.text = "${binding.expression.text}1"
        }
    }
}