package com.example.calculate.util

class CalcUtil {
    fun getResult(expr: String): Double {
        val calcUnit: List<String> = expr.split(" ")
        val handleUnits = ArrayList<String>()
        val result = ArrayList<String>()
        var i = 0
        while (i < calcUnit.size) {
            if (calcUnit[i] == "×") {
                handleUnits.set(
                    handleUnits.size-1,
                    (handleUnits[handleUnits.size-1].toDouble() * calcUnit[i+1].toDouble()).toString()
                )
                i++
            } else if (calcUnit[i] == "÷") {
                handleUnits.set(
                    handleUnits.size-1,
                    (handleUnits[handleUnits.size-1].toDouble() / calcUnit[i+1].toDouble()).toString()
                )
                i++
            } else {
                handleUnits.add(calcUnit[i])
            }
            i++
        }

        i = 0
        while (i < handleUnits.size) {
            if (handleUnits[i] == "+") {
                result.set(
                    result.size-1,
                    (result[result.size-1].toDouble() + handleUnits[i+1].toDouble()).toString()
                )
                i++
            } else if (handleUnits[i] == "-") {
                result.set(
                    result.size-1,
                    (result[result.size-1].toDouble() - handleUnits[i+1].toDouble()).toString()
                )
                i++
            } else {
                result.add(handleUnits[i])
            }
            i++
        }
        return result[0].toDouble()
    }
}