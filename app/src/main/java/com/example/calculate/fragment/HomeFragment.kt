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

    var a: String = ""
    var list = mutableListOf<String>()
    var b: Long = 0L
    var c: Long = 1L
    var d: Double = 1.0

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
            if (binding.expression.text.isEmpty()) {
                binding.expression.text = "${binding.expression.text}0."
            } else if (binding.expression.text.last() == '±' || binding.expression.text.last() == '('
                || binding.expression.text.last() == ')' || binding.expression.text.last() == '%' || binding.expression.text.last() == '÷'
                || binding.expression.text.last() == '×'  || binding.expression.text.last() == '-'  || binding.expression.text.last() == '+') {
                binding.expression.text = "${binding.expression.text}0."
            } else if (binding.expression.text.last() == '.') {
                binding.expression.text = "${binding.expression.text}"
            } else {
                binding.expression.text = "${binding.expression.text}."
            }

            binding.answer.text = ""
        }

        binding.buttonPosNeg.setOnClickListener {
            if (binding.expression.text.isEmpty()) {
                binding.expression.text = "${binding.expression.text}(-"
            } else if (binding.expression.text.last() == '-' ) {
                var str = binding.expression.text.toString()
                str = str.substring(0, str.length-1)

                if (str.last() == '(') {
                    str = str.substring(0, str.length-1)
                    binding.expression.text = str
                } else {
                    binding.expression.text = "${binding.expression.text}(-"
                }
            } else {
                binding.expression.text = "${binding.expression.text}(-"
            }

            binding.answer.text = ""
        }

        binding.buttonParentheses.setOnClickListener {
            binding.expression.text = "${binding.expression.text}()"

            binding.answer.text = ""
        }

        binding.buttonPercent.setOnClickListener {
            if (binding.expression.text.isEmpty()) {
                Toast.makeText(context, "완성되지 않은 수식입니다", Toast.LENGTH_SHORT).show()
            } else if (binding.expression.text.last() == '.' || binding.expression.text.last() == '±' || binding.expression.text.last() == '('
                || binding.expression.text.last() == ')' || binding.expression.text.last() == '%' || binding.expression.text.last() == '÷'
                || binding.expression.text.last() == '×'  || binding.expression.text.last() == '-'  || binding.expression.text.last() == '+') {
                binding.expression.text = "${binding.expression.text}"
            } else {
                binding.expression.text = "${binding.expression.text}%"
            }

            binding.answer.text = ""
        }

        binding.buttonDivide.setOnClickListener {
            if (binding.expression.text.isEmpty()) {
                Toast.makeText(context, "완성되지 않은 수식입니다", Toast.LENGTH_SHORT).show()
            } else if (binding.expression.text.last() == '.' || binding.expression.text.last() == '±' || binding.expression.text.last() == '('
                || binding.expression.text.last() == ')' || binding.expression.text.last() == '%' || binding.expression.text.last() == '÷'
                || binding.expression.text.last() == '×'  || binding.expression.text.last() == '-'  || binding.expression.text.last() == '+') {
                binding.expression.text = "${binding.expression.text}"
            } else {
                binding.expression.text = "${binding.expression.text}÷"
            }

            binding.answer.text = ""
        }

        binding.buttonMultiply.setOnClickListener {
            if (binding.expression.text.isEmpty()) {
                Toast.makeText(context, "완성되지 않은 수식입니다", Toast.LENGTH_SHORT).show()
            } else if (binding.expression.text.last() == '.' || binding.expression.text.last() == '±' || binding.expression.text.last() == '('
                || binding.expression.text.last() == ')' || binding.expression.text.last() == '%' || binding.expression.text.last() == '÷'
                || binding.expression.text.last() == '×'  || binding.expression.text.last() == '-'  || binding.expression.text.last() == '+') {
                binding.expression.text = "${binding.expression.text}"
            } else {
                binding.expression.text = "${binding.expression.text}×"
            }

            binding.answer.text = ""
        }

        binding.buttonMinus.setOnClickListener {
            if (binding.expression.text.isEmpty()) {
                Toast.makeText(context, "완성되지 않은 수식입니다", Toast.LENGTH_SHORT).show()
            } else if (binding.expression.text.last() == '.' || binding.expression.text.last() == '±' || binding.expression.text.last() == '('
                || binding.expression.text.last() == ')' || binding.expression.text.last() == '%' || binding.expression.text.last() == '÷'
                || binding.expression.text.last() == '×'  || binding.expression.text.last() == '-'  || binding.expression.text.last() == '+') {
                binding.expression.text = "${binding.expression.text}"
            } else {
                binding.expression.text = "${binding.expression.text}-"
            }

            binding.answer.text = ""
        }

        binding.buttonPlus.setOnClickListener {
            if (binding.expression.text.isEmpty()) {
                Toast.makeText(context, "완성되지 않은 수식입니다", Toast.LENGTH_SHORT).show()
            } else if (binding.expression.text.last() == '.' || binding.expression.text.last() == '±' || binding.expression.text.last() == '('
                || binding.expression.text.last() == ')' || binding.expression.text.last() == '%' || binding.expression.text.last() == '÷'
                || binding.expression.text.last() == '×'  || binding.expression.text.last() == '-'  || binding.expression.text.last() == '+') {
                binding.expression.text = "${binding.expression.text}"
            } else {
                binding.expression.text = "${binding.expression.text}+"
            }

            binding.answer.text = ""
        }

        binding.buttonEqual.setOnClickListener {
            equal_btn()

            binding.expression.text = "${binding.answer.text}"
            binding.answer.text = ""
        }
    }

    fun equal_btn() {
        b = 0
        c = 1
        d = 1.0
        if (list.size != 0) {
            list = mutableListOf<String>()
        }

        if (binding.expression.text.contains("+")) {
            val str = binding.expression.text.split("+")
            val a = str[0].toDouble()
            val z = a.toInt()

            for (i in 0..str.size - 1) {
                if(i == 0){
                    list.add(z.toString())
                    b += list.get(i).toInt()
                }else{
                    list.add(str[i])
                    b += list.get(i).toInt()
                }

            }

            binding.answer.text = String.format("%d", b)
        }

        if (binding.expression.text.contains("-")) {
            val str = binding.expression.text.split("-")
            val a = str[0].toDouble()
            val z = a.toLong()
            b = z
            for (i in 0..str.size - 1) {
                list.add(str[i])
                if (i != 0) {
                    b -= list.get(i).toInt()

                }
            }

            binding.answer.text = String.format("%d", b)
        }

        if (binding.expression.text.contains("×")) {
            val str = binding.expression.text.split("×")
            val a = str[0].toDouble()
            val z = a.toInt()

            for (i in 0..str.size - 1) {
                if(i == 0){
                    list.add(z.toString())
                    c *= list.get(i).toInt()
                }else{
                    list.add(str[i])
                    c *= list.get(i).toInt()
                }
            }

            binding.answer.text = String.format("%d", c)
        }

        if (binding.expression.text.contains("÷")) {
            val str = binding.expression.text.split("÷")
            d = str[0].toDouble()
            for (i in 0..str.size - 1) {
                list.add(str[i])
                if (i != 0) {
                    d /= list.get(i).toDouble()
                }
            }

            binding.answer.text = String.format("%f", d)
        }
    }
}