package com.example.calculate.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ScrollView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.example.calculate.R
import com.example.calculate.database.CalculateApplication
import com.example.calculate.database.CalculateDatabase
import com.example.calculate.databinding.FragmentHomeBinding
import com.example.calculate.model.SharedViewModel
import com.example.calculate.model.calculate
import com.example.calculate.util.CalcUtil
import com.example.calculate.viewModel.CalculateViewModel
import com.example.calculate.viewModel.CalculateViewModelFactory

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!
    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var calculateViewModel: CalculateViewModel

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
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val dao = (requireActivity().application as CalculateApplication).database.getCalculateDao()
        calculateViewModel = ViewModelProvider(this, CalculateViewModelFactory(dao)).get(CalculateViewModel::class.java)

        sharedViewModel.expression.observe (viewLifecycleOwner) { data ->
            homeBinding!!.expression.setText(data)
        }

        binding.pastTimeBtn.setOnClickListener {
            sharedViewModel.expression.value = homeBinding!!.expression.text.toString()

            it.findNavController().navigate(R.id.action_HomeFragment_to_pastCalculateFragment, null,
                NavOptions.Builder()
                    .setPopUpTo(R.id.HomeFragment, true)
                    .build())
        }

        btn()
    }

    fun btn() {
        val numberBtns : Array<Button> = arrayOf(
            homeBinding!!.button0,
            homeBinding!!.button1,
            homeBinding!!.button2,
            homeBinding!!.button3,
            homeBinding!!.button4,
            homeBinding!!.button5,
            homeBinding!!.button6,
            homeBinding!!.button7,
            homeBinding!!.button8,
            homeBinding!!.button9
        )

        for (btn in numberBtns) {
            btn.setOnClickListener {
                sharedViewModel.expression.value = sharedViewModel.expression.value?.let { it1 -> addDigits(it1, btn.text[0]) }
            }
        }

        val operatorBtns : Array<Button> = arrayOf(
            homeBinding!!.buttonPlus,
            homeBinding!!.buttonMinus,
            homeBinding!!.buttonMultiply,
            homeBinding!!.buttonDivide,
            homeBinding!!.buttonPoint
        )

        for (btn in operatorBtns) {
            btn.setOnClickListener {
                sharedViewModel.expression.value = sharedViewModel.expression.value?.let { it1 -> addDigits(it1, btn.text[0]) }
            }
        }

        homeBinding!!.buttonC.setOnClickListener {
            sharedViewModel.expression.value = ""
        }

        homeBinding!!.buttonEqual.setOnClickListener {
            if (homeBinding!!.expression.text.isEmpty()) {

            } else if (homeBinding!!.expression.text.isNotEmpty() and isOperator(homeBinding!!.expression.text.last())) {
                Toast.makeText(context, "완성되지 않은 수식입니다", Toast.LENGTH_SHORT).show()
            } else if (homeBinding!!.expression.text.isNotEmpty()) {
                val expression = binding.expression.text.toString()
                val util = CalcUtil()
                sharedViewModel.expression.value = sharedViewModel.expression.value?.let { it1 -> util.getResult(it1).toString() }
                val answer = binding.expression.text.toString()

                val calculation = calculate(0, expression, answer)
                calculateViewModel.insert(calculation)
            } else {

            }
        }

        homeBinding!!.backspace.setOnClickListener {
            var str = binding.expression.text.toString()

            if (str.isNotEmpty()) {
                str = str.substring(0, str.length-1)
                sharedViewModel.expression.value = "${str}"

                for (i in str) {
                    if (str.last() == ' ') {
                        str = str.substring(0, str.length-1)
                        sharedViewModel.expression.value = "${str}"
                    } else {
                        break
                    }
                }
            }
        }

        homeBinding!!.buttonParentheses.setOnClickListener {
            val currentExpression = sharedViewModel.expression.value ?: ""

            val openCount = currentExpression.count { it == '(' }
            val closeCount = currentExpression.count { it == ')' }

            if (openCount > closeCount) {
                sharedViewModel.expression.value = addDigits(currentExpression, ')')
            } else {
                sharedViewModel.expression.value = addDigits(currentExpression, '(')
            }
        }
    }

    private fun isNumber(digit: Char): Boolean {
        return (digit == '0') or
                (digit == '1') or
                (digit == '2') or
                (digit == '3') or
                (digit == '4') or
                (digit == '5') or
                (digit == '6') or
                (digit == '7') or
                (digit == '8') or
                (digit == '9')
    }

    private fun isOperator(digit: Char): Boolean {
        return (digit == '+') or
                (digit == '-') or
                (digit == '×') or
                (digit == '÷')
    }

    private fun addDigits(prevState: String, digit: Char): String {
        return when {
            isOperator(digit) && prevState.isEmpty() -> prevState

            prevState.isEmpty() && digit == '.' -> "0$digit"

            (prevState == "0" || prevState == "0.0" || prevState.isEmpty()) -> "$prevState$digit"

            isNumber(prevState.last()) && isNumber(digit) -> "$prevState$digit"

            isNumber(prevState.last()) && digit == '.' -> "$prevState$digit"

            prevState.last() == '.' && isNumber(digit) -> "$prevState$digit"

            prevState.last() == '.' && digit == '.' -> prevState

            isOperator(prevState.last()) && digit == '.' -> "$prevState 0$digit"

            isOperator(prevState.last()) && isOperator(digit) -> prevState

            isOperator(prevState.last()) && digit == ')' -> "$prevState ("

            prevState.last() == '(' && (digit == '×' || digit == '÷') -> prevState

            prevState.last() == '(' && digit == '.' -> "$prevState 0$digit"

            prevState.last() == '(' && digit == ')' -> prevState

            isNumber(prevState.last()) && digit == '(' -> "$prevState × $digit"

            prevState.last() == ')' && isNumber(digit) -> "$prevState × $digit"

            prevState.last() == ')' && digit == '(' -> "$prevState × $digit"

            digit == ')' && prevState.count { it == '(' } > prevState.count { it == ')' } -> "$prevState $digit"

            digit == '(' && (prevState.isEmpty() || isOperator(prevState.last()) || prevState.last() == '(') -> "$prevState $digit"

            else -> "$prevState $digit"
        }
    }
}