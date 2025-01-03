package com.example.calculate.fragment

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calculate.R
import com.example.calculate.adapter.calculateAdapter
import com.example.calculate.database.CalculateApplication
import com.example.calculate.databinding.FragmentPastCalculateBinding
import com.example.calculate.model.SharedViewModel
import com.example.calculate.model.calculate
import com.example.calculate.util.CalcUtil
import com.example.calculate.viewModel.CalculateViewModel
import com.example.calculate.viewModel.CalculateViewModelFactory
import java.text.DecimalFormat

@RequiresApi(Build.VERSION_CODES.O)
class pastCalculateFragment : Fragment(R.layout.fragment_past_calculate) {

    private var pastfragment: FragmentPastCalculateBinding? = null
    private val binding get() = pastfragment!!

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var calculateViewModel: CalculateViewModel

    private lateinit var calculateAdapter: calculateAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        pastfragment = FragmentPastCalculateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val dao = (requireActivity().application as CalculateApplication).database.getCalculateDao()
        calculateViewModel = ViewModelProvider(this, CalculateViewModelFactory(dao)).get(CalculateViewModel::class.java)

        sharedViewModel.expression.observe (viewLifecycleOwner) { data ->
            pastfragment!!.psExpression.setText(data)
        }

        binding.calculate.setOnClickListener {
            it.findNavController().navigate(R.id.action_pastCalculateFragment_to_HomeFragment, null,
            NavOptions.Builder()
                .setPopUpTo(R.id.pastCalculateFragment, true)
                .build())
        }

        initRecycler()

        btn()
    }

    private fun autoScrollToBottom() {
        binding.psScrollView.post {
            binding.psScrollView.fullScroll(View.FOCUS_DOWN)
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
        val operatorBtns : Array<Button> = arrayOf(
            pastfragment!!.buttonPlus,
            pastfragment!!.buttonMinus,
            pastfragment!!.buttonMultiply,
            pastfragment!!.buttonDivide
        )

        for (btn in operatorBtns) {
            btn.setOnClickListener {
                sharedViewModel.expression.value = sharedViewModel.expression.value?.let { it1 -> addDigits(it1, btn.text[0]) }
                autoScrollToBottom()
            }
        }

        pastfragment!!.buttonEqual.setOnClickListener {
            if (pastfragment!!.psExpression.text.isEmpty()) {

            } else if (pastfragment!!.psExpression.text.isNotEmpty() and isOperator(pastfragment!!.psExpression.text.last())) {
                Toast.makeText(context, "완성되지 않은 수식입니다", Toast.LENGTH_SHORT).show()
            } else if (pastfragment!!.psExpression.text.isNotEmpty()) {
                val expression = binding.psExpression.text.toString()
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

        pastfragment!!.psBackspace.setOnClickListener {
            var str = binding.psExpression.text.toString()

            if (str.isNotEmpty()) {
                if (str.last() == ',') {
                    str = str.substring(0, str.length - 2)
                } else {
                    str = str.substring(0, str.length - 1)
                }

                sharedViewModel.expression.value = formatExpressionWithCommas(str)
                pastfragment!!.psExpression.text = sharedViewModel.expression.value
                autoScrollToBottom()
            }
        }

        pastfragment!!.deletePast.setOnClickListener {
            calculateViewModel.deleteAll()
        }
    }

    private fun isOperator(digit: Char): Boolean {
        return (digit == '+') or
                (digit == '-') or
                (digit == '×') or
                (digit == '÷')
    }

    private fun addDigits(prevState :String, digit: Char): String {
        return if (isOperator(digit) and (prevState == "")) "$prevState"

        else if ((prevState == "0") or (prevState == "0.0") or (prevState == "")) "$prevState$digit"

        else if (isOperator(prevState.last()) and isOperator(digit)) "$prevState"

        else "$prevState $digit"
    }

    private fun initRecycler() {
        calculateAdapter = calculateAdapter()

        binding.recyclerview.apply {
            val adapter = calculateAdapter
            binding.recyclerview.adapter = adapter
            val manager =LinearLayoutManager(context)

            setHasFixedSize(true)

            binding.recyclerview.layoutManager = manager
        }

        calculateViewModel.allCalculations.observe(viewLifecycleOwner) { calculations ->
            calculateAdapter.differ.clear()
            calculateAdapter.differ.addAll(calculations)
            calculateAdapter.notifyDataSetChanged()
        }
    }
}