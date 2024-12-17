package com.example.calculate.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.example.calculate.R
import com.example.calculate.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!
    private var liveExpr : MutableLiveData<String> = MutableLiveData("")

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

        liveExpr.observe (viewLifecycleOwner) { data ->
            homeBinding!!.expression.setText(data)
        }

        binding.pastTimeBtn.setOnClickListener {
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
                liveExpr.value = liveExpr.value?.let { it1 -> addDigits(it1, btn.text[0]) }
            }
        }

        val operatorBtns : Array<Button> = arrayOf(
            homeBinding!!.buttonPlus,
            homeBinding!!.buttonMinus,
            homeBinding!!.buttonMultiply,
            homeBinding!!.buttonDivide,
            homeBinding!!.buttonPoint,
            homeBinding!!.buttonPercent
        )

        for (btn in operatorBtns) {
            btn.setOnClickListener {
                liveExpr.value = liveExpr.value?.let { it1 -> addDigits(it1, btn.text[0]) }
            }
        }

        homeBinding!!.buttonC.setOnClickListener {
            liveExpr.value = ""
            homeBinding!!.answer.text = ""
        }

        homeBinding!!.buttonEqual.setOnClickListener {
            liveExpr.value = "${homeBinding!!.answer.text}"
        }

        homeBinding!!.backspace.setOnClickListener {
            var str = binding.expression.text.toString()

            if (str.isNotEmpty()) {
                str = str.substring(0, str.length-1)
                liveExpr.value = "${str}"
            }
        }

        homeBinding!!.buttonParentheses.setOnClickListener {
            if (homeBinding!!.expression.text.contains('(')) {
                liveExpr.value = liveExpr.value?.let { it1 -> addDigits(it1, homeBinding!!.buttonParentheses.text[1]) }
            } else {
                liveExpr.value = liveExpr.value?.let { it1 -> addDigits(it1, homeBinding!!.buttonParentheses.text[0]) }
            }
        }

        homeBinding!!.buttonPosNeg.setOnClickListener {
            val text = "(-"

            if (homeBinding!!.expression.text.isEmpty()) {
                liveExpr.value = liveExpr.value?.let { it1 -> addDigits(it1, text[0]) }
                liveExpr.value = liveExpr.value?.let { it1 -> addDigits(it1, text[1]) }
            } else if (homeBinding!!.expression.text.last() == '-') {
                var str = binding.expression.text.toString()

                if (str.isNotEmpty()) {
                    str = str.substring(0, str.length-1)

                    if (str.last() == '(') {
                        str = str.substring(0, str.length-1)
                        liveExpr.value = "${str}"
                    } else {
                        liveExpr.value = liveExpr.value?.let { it1 -> addDigits(it1, text[0]) }
                        liveExpr.value = liveExpr.value?.let { it1 -> addDigits(it1, text[1]) }
                    }
                }
            } else {
                liveExpr.value = liveExpr.value?.let { it1 -> addDigits(it1, text[0]) }
                liveExpr.value = liveExpr.value?.let { it1 -> addDigits(it1, text[1]) }
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

    private fun addDigits(prevState :String, digit: Char): String {
        return if ((prevState == "0") or (prevState == "0.0") or (prevState == "")) "$digit"

        else if (isNumber(prevState.last()) and isNumber(digit)) "$prevState$digit"

        else "$prevState$digit"
    }
}