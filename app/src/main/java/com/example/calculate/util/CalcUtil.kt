package com.example.calculate.util

class CalcUtil {
    fun getResult(expr: String): Number {
        val tokens = expr.split(" ")
        val outputQueue = mutableListOf<String>()
        val operatorStack = mutableListOf<String>()

        val precedence = mapOf(
            "+" to 1,
            "-" to 1,
            "×" to 2,
            "÷" to 2
        )

        for (token in tokens) {
            when {
                token.toDoubleOrNull() != null -> outputQueue.add(token)
                token == "(" -> operatorStack.add(token)
                token == ")" -> {
                    while (operatorStack.isNotEmpty() && operatorStack.last() != "(") {
                        outputQueue.add(operatorStack.removeAt(operatorStack.size - 1))
                    }
                    operatorStack.removeAt(operatorStack.size - 1)
                }
                else -> {
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
                    val right = resultStack.removeAt(resultStack.size - 1)
                    val left = resultStack.removeAt(resultStack.size - 1)
                    val result = when (token) {
                        "+" -> left + right
                        "-" -> left - right
                        "×" -> left * right
                        "÷" -> left / right
                        else -> throw IllegalArgumentException("Unknown operator: $token")
                    }
                    resultStack.add(result)
                }
            }
        }

        val finalResult = resultStack.last()
        return if (finalResult % 1.0 == 0.0) {
            finalResult.toInt()
        } else {
            finalResult
        }
    }
}