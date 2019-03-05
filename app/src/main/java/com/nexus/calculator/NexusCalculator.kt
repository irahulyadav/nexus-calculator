package com.nexus.calculator

import java.util.*

class NexusCalculator {

    companion object {

        fun isNumber(char: Char): Boolean {
            return !(char in arrayListOf('+', '-', '*', '/', '(', ')'))
        }


        fun evaluate(expression: String): Double {
            val inputes = expression.toCharArray()
            val values = Stack<Double>()

            val ops = Stack<Char>()

            var i = 0
            try {
                while (i < inputes.size) {

                    if (inputes[i] == ' ') {
                        i++
                        continue
                    }

                    if (isNumber(inputes[i])) {
                        val num = StringBuffer()
                        while (i < inputes.size && (isNumber(inputes[i])))
                            num.append(inputes[i++])
                        if (values.isEmpty() && !ops.isEmpty() && ops.peek() == '-') {
                            ops.pop()
                            values.push(num.toString().toDouble() * -1)
                            continue
                        }
                        values.push(num.toString().toDouble())
                        continue
                    } else if (inputes[i] == '(') {
                        ops.push(inputes[i])
                    } else if (inputes[i] == ')') {
                        while (ops.peek() != '(')
                            values.push(operation(ops.pop(), values.pop(), values.pop()))
                        ops.pop()
                    } else if (inputes[i] in arrayOf('+', '-', '*', '/')) {

                        while (!ops.empty() && hasPrecedence(ops.peek(), inputes[i])) {
                            values.push(operation(ops.pop(), values.pop(), values.pop()))
                        }

                        ops.push(inputes[i])
                    }
                    i++
                }

                while (!ops.empty() && !values.isEmpty())
                    values.push(operation(ops.pop(), values.pop(), values.pop()))
            } catch (ex: Exception) {
                throw InvalidExpression()
            }

            return values.pop()
        }

        fun hasPrecedence(op1: Char, op2: Char): Boolean {
            if (op2 == '(' || op2 == ')')
                return false

            return (op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')
        }

        fun operation(op: Char, b: Double, a: Double): Double {
            //println("$a $op $b")
            when (op) {
                '+' -> return a + b
                '-' -> return a - b
                '*' -> return a * b
                '/' -> {
                    if (b == 0.0)
                        throw UnsupportedOperationException("Cannot divide by zero")
                    return a / b
                }
            }
            return 0.0
        }
    }


}