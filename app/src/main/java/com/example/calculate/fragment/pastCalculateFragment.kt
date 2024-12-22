package com.example.calculate.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.findFragment
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calculate.R
import com.example.calculate.adapter.calculateAdapter
import com.example.calculate.databinding.FragmentPastCalculateBinding
import com.example.calculate.model.calculate
import com.example.calculate.util.CalcUtil

@RequiresApi(Build.VERSION_CODES.O)
class pastCalculateFragment : Fragment(R.layout.fragment_past_calculate) {

    private var pastfragment: FragmentPastCalculateBinding? = null
    private val binding get() = pastfragment!!
    private var liveExpr : MutableLiveData<String> = MutableLiveData("")

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

        liveExpr.observe (viewLifecycleOwner) { data ->
            pastfragment!!.psExpression.setText(data)
        }

        binding.calculate.setOnClickListener {
            it.findNavController().navigate(R.id.action_pastCalculateFragment_to_HomeFragment, null,
            NavOptions.Builder()
                .setPopUpTo(R.id.pastCalculateFragment, true)
                .build())
        }

        initRecycler()
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
                liveExpr.value = liveExpr.value?.let { it1 -> addDigits(it1, btn.text[0]) }
            }
        }

        pastfragment!!.buttonEqual.setOnClickListener {
            if (pastfragment!!.psExpression.text.isEmpty()) {

            } else if (pastfragment!!.psExpression.text.isNotEmpty() and isOperator(pastfragment!!.psExpression.text.last())) {
                Toast.makeText(context, "완성되지 않은 수식입니다", Toast.LENGTH_SHORT).show()
            } else if (pastfragment!!.psExpression.text.isNotEmpty()) {
                val util = CalcUtil()
                liveExpr.value = liveExpr.value?.let { it1 -> util.getResult(it1).toString() }
            } else {

            }
        }

        pastfragment!!.psBackspace.setOnClickListener {
            var str = binding.psExpression.text.toString()

            if (str.isNotEmpty()) {
                str = str.substring(0, str.length-1)
                liveExpr.value = "${str}"

                for (i in str) {
                    if (str.last() == ' ') {
                        str = str.substring(0, str.length-1)
                        liveExpr.value = "${str}"
                    } else {
                        break
                    }
                }
            }
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