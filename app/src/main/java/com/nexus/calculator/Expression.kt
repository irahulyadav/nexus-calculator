package com.nexus.calculator

class Expression {
    val isLastOperation: Boolean
        get() = last?.isOperation ?: false

    var allowDecimal = true

    var last: NumberItem? = null

    var expressionCount = 0

    fun add(text: String, value: NumberItem, result: (String) -> Unit) {
        val type = value.type

        if (type == NumberType.clean) {
            result("")
            last = null
            return
        }
        if (type == NumberType.result) {
            if (!text.isEmpty()) {
                last = NumberItem('0')
                try {
                    result(NexusCalculator.evaluate(text).toString())
                } catch (ex: InvalidExpression) {

                }

            }
            return
        }

        if (type == NumberType.back) {
            result(removeLastChar(text))
            last = if (text.isEmpty()) null else NumberItem(text.toCharArray().last())
            return
        }

        //not allowed '0(', '.(', ')(',
        if (value.value == '(' && !(isLastOperation || last?.value == '(')) {
            return
        }

        if (value.value == ')' && (expressionCount == 0 || !(last?.value == ')' || last?.isOnlyNumber == true))) {
            return
        }

        if (type == NumberType.number && last?.value == ')') {
            return
        }

        if (value.value == '.' && !allowDecimal) {
            return
        }

        if (last?.value == '(' && !(value.value == '-' || value.value == '(')) {
            last = value
            return
        }

        if (type != NumberType.number && ((last == null && (value.value == '*' || value.value == '\\')) || (last?.value == '.'))) {
            return
        }

        val replaceLast = value.isOperation && isLastOperation
        last = value

        if (replaceLast) {
            if (text.length > 1 || last?.value == '-' || last?.value == '+') {
                result(removeLastChar(text) + value.value)
            }
            return
        }
        result(text + value.value)
        allowDecimal = if (value.isNumber && value.value != '.') allowDecimal else value.value != '.'
        if (value.value == '(') {
            expressionCount++
        } else if (value.value == ')') {
            expressionCount--
        }
    }

    private fun removeLastChar(str: String): String {
        return if (str.isEmpty()) "" else str.substring(0, str.length - 1)
    }
}