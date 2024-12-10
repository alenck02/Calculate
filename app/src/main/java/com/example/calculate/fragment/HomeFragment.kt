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

        binding.backspace.setOnClickListener {
            var str = binding.expression.text.toString()
            if (str.isNotEmpty()) {
                str = str.substring(0, str.length-1)
                binding.expression.text = str
            }
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

        binding.button2.setOnClickListener {
            binding.expression.text = "${binding.expression.text}2"
        }

        binding.button3.setOnClickListener {
            binding.expression.text = "${binding.expression.text}3"
        }

        binding.button4.setOnClickListener {
            binding.expression.text = "${binding.expression.text}4"
        }

        binding.button5.setOnClickListener {
            binding.expression.text = "${binding.expression.text}5"
        }

        binding.button6.setOnClickListener {
            binding.expression.text = "${binding.expression.text}6"
        }

        binding.button7.setOnClickListener {
            binding.expression.text = "${binding.expression.text}7"
        }

        binding.button8.setOnClickListener {
            binding.expression.text = "${binding.expression.text}8"
        }

        binding.button9.setOnClickListener {
            binding.expression.text = "${binding.expression.text}9"
        }

        binding.buttonPoint.setOnClickListener {
            binding.expression.text = "${binding.expression.text}."
        }

        binding.buttonPosNeg.setOnClickListener {
            binding.expression.text = "${binding.expression.text}±"
        }

        binding.buttonParentheses.setOnClickListener {
            binding.expression.text = "${binding.expression.text}()"
        }

        binding.buttonPercent.setOnClickListener {
            binding.expression.text = "${binding.expression.text}%"
        }

        binding.buttonDivide.setOnClickListener {
            binding.expression.text = "${binding.expression.text}÷"
        }

        binding.buttonMultiply.setOnClickListener {
            binding.expression.text = "${binding.expression.text}×"
        }

        binding.buttonMinus.setOnClickListener {
            binding.expression.text = "${binding.expression.text}-"
        }

        binding.buttonPlus.setOnClickListener {
            binding.expression.text = "${binding.expression.text}+"
        }

        binding.buttonEqual.setOnClickListener {
            binding.expression.text = "${binding.answer.text}"
            binding.answer.text = ""
        }
    }
}