package com.example.calculate.util

import java.text.DecimalFormat

class CalcUtil {
    fun getResult(expr: String): String {
        val sanitizedExpr = expr.replace(",", "")
        val tokens = sanitizedExpr.split(" ")
        val outputQueue = mutableListOf<String>()
        val operatorStack = mutableListOf<String>()

        val precedence = mapOf(
            "+" to 1,
            "-" to 1,
            "×" to 2,
            "÷" to 2
        )

        try {
            for (token in tokens) {
                when {
                    token.toDoubleOrNull() != null -> outputQueue.add(token)
                    token == "(" -> operatorStack.add(token)
                    token == ")" -> {
                        while (operatorStack.isNotEmpty() && operatorStack.last() != "(") {
                            outputQueue.add(operatorStack.removeAt(operatorStack.size - 1))
                        }
                        if (operatorStack.isEmpty()) {
                            return "Error: Mismatched parentheses"
                        }
                        operatorStack.removeAt(operatorStack.size - 1)
                    }
                    else -> {
                        if (!precedence.containsKey(token)) {
                            return "Error: Unknown operator $token"
                        }
                        while (operatorStack.isNotEmpty() && precedence[operatorStack.last()] ?: 0 >= precedence[token] ?: 0) {
                            outputQueue.add(operatorStack.removeAt(operatorStack.size - 1))
                        }
                        operatorStack.add(token)
                    }
                }
            }

            while (operatorStack.isNotEmpty()) {
                outputQueue.add(operatorStack.removeAt(operatorStack.size - 1))
            }

            val resultStack = mutableListOf<Double>()
            for (token in outputQueue) {
                when {
                    token.toDoubleOrNull() != null -> resultStack.add(token.toDouble())
                    else -> {
                        if (resultStack.size < 2) {
                            return "Error: Insufficient values for operation"
                        }
                        val right = resultStack.removeAt(resultStack.size - 1)
                        val left = resultStack.removeAt(resultStack.size - 1)
                        val result = when (token) {
                            "+" -> left + right
                            "-" -> left - right
                            "×" -> left * right
                            "÷" -> left / right
                            else -> return "Error: Unknown operator $token"
                        }
                        resultStack.add(result)
                    }
                }
            }

            if (resultStack.size != 1) {
                return "Error: Invalid expression"
            }

            val finalResult = resultStack.last()
            return formatNumber(finalResult)
        } catch (e: Exception) {
            return "Error: ${e.message}"
        }
    }

    private fun formatNumber(number: Double): String {
        val decimalFormat = DecimalFormat("#,###.##")
        return decimalFormat.format(number)
    }
}