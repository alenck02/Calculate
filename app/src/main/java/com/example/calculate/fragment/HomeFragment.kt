package com.example.calculate.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.example.calculate.R
import com.example.calculate.database.CalculateApplication
import com.example.calculate.databinding.FragmentHomeBinding
import com.example.calculate.model.SharedViewModel
import com.example.calculate.model.calculate
import com.example.calculate.util.CalcUtil
import com.example.calculate.viewModel.CalculateViewModel
import com.example.calculate.viewModel.CalculateViewModelFactory
import java.text.DecimalFormat

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

    private fun autoScrollToBottom() {
        binding.scrollView.post {
            binding.scrollView.fullScroll(View.FOCUS_DOWN)
        }
    }

    private fun formatNumberWithComma(number: String): String {
        val cleanedNumber = number.replace(",", "")
        return if (cleanedNumber.isNotEmpty() && cleanedNumber.all { it.isDigit() }) {
            DecimalFormat("#,###").format(cleanedNumber.toDouble())
        } else {
            number
        }
    }

    private fun formatExpressionWithCommas(expression: String): String {
        val tokens = expression.split(" ")
        return tokens.joinToString(" ") { token ->
            if (token.any { it.isDigit() }) {
                formatNumberWithComma(token)
            } else {
                token
            }
        }
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
                sharedViewModel.expression.value = sharedViewModel.expression.value?.let { it1 ->
                    addDigits(it1, btn.text[0])
                }

                homeBinding!!.expression.text = formatExpressionWithCommas(sharedViewModel.expression.value ?: "")
                autoScrollToBottom()
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
                sharedViewModel.expression.value = sharedViewModel.expression.value?.let { it1 ->
                    val newState = addDigits(it1, btn.text[0])

                    formatExpressionWithCommas(newState)
                }
                autoScrollToBottom()
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

                val result = util.getResult(expression)

                if (result.startsWith("Error:")) {
                    Toast.makeText(context, result, Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                sharedViewModel.expression.value = result
                val answer = result

                val calculation = calculate(0, expression, answer)
                calculateViewModel.insert(calculation)
            }
        }

        homeBinding!!.backspace.setOnClickListener {
            var str = binding.expression.text.toString()

            if (str.isNotEmpty()) {
                if (str.last() == ',') {
                    str = str.substring(0, str.length - 2)
                } else {
                    str = str.substring(0, str.length - 1)
                }

                sharedViewModel.expression.value = formatExpressionWithCommas(str)
                homeBinding!!.expression.text = sharedViewModel.expression.value
                autoScrollToBottom()
            }
        }

        homeBinding!!.buttonParentheses.setOnClickListener {
            val currentExpression = sharedViewModel.expression.value ?: ""

            val openCount = currentExpression.count { it == '(' }
            val closeCount = currentExpression.count { it == ')' }

            val newState = if (openCount > closeCount) {
                addDigits(currentExpression, ')')
            } else {
                addDigits(currentExpression, '(')
            }

            sharedViewModel.expression.value = newState
            homeBinding!!.expression.text = formatExpressionWithCommas(newState)
            autoScrollToBottom()
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

            prevState.last() == '(' && digit == '(' -> "$prevState $digit"

            prevState.last() == ')' && digit == '.' -> "$prevState × 0$digit"

            isNumber(prevState.last()) && digit == '(' -> "$prevState × $digit"

            prevState.last() == ')' && isNumber(digit) -> "$prevState × $digit"

            prevState.last() == ')' && digit == '(' -> "$prevState × $digit"

            digit == ')' && prevState.count { it == '(' } > prevState.count { it == ')' } -> "$prevState $digit"

            digit == '(' && (prevState.isEmpty() || isOperator(prevState.last()) || prevState.last() == '(') -> "$prevState $digit"

            else -> "$prevState $digit"
        }
    }
}