package com.nexus.calculator

import org.junit.Assert.assertEquals
import org.junit.Test

class NexusCalculatorUnitTest {

    val list = arrayListOf(
        "4 + 3 + 4",
        "3-2",
        "-7.9 + 2.2",
        "3 + 2 * 3",
        "3 * 2 + 3",
        "3 + ( 2 * 3) + ((5/3)-1)",
        "3 + ( 2 * 3 + ((5/3)-1)"
    )

    val results = arrayListOf<Double>(
        4 + 3 + 4.0,
        3 - 2.0, -7.9 + 2.2,
        3.0 + 2 * 3,
        3 * 2 + 3.0,
        3.0 + (2 * 3) + ((5.0 / 3) - 1),
        3.0 + (2 * 3) + ((5.0 / 3) - 1)
    )


    @Test
    fun evaluate() {
        val i = 4
        println("${list[i]} = ${NexusCalculator.evaluate(list[i])}")
        assertEquals("${list[i]} = ${results[i]}", results[i], NexusCalculator.evaluate(list[i]), 0.001)
    }

    @Test
    fun print_Evaluate() {
        var i = 0
        while (i < list.size - 1) {
            println("${list[i]} = ${NexusCalculator.evaluate(list[i++])}")
        }
    }

    @Test
    fun check_Evaluate() {
        var i = 0
        while (i < list.size) {
            try {
                assertEquals("${list[i]} = ${results[i]}", results[i], NexusCalculator.evaluate(list[i++]), 0.001)
            } catch (ex: InvalidExpression) {
                assert(i == list.size - 1)
            }

        }
    }

    @Test
    fun check_Operation() {
        val a = 5317.16394
        val b = 1422.23564
        assertEquals("$a + $b = ${a + b}", a + b, NexusCalculator.operation('+', b, a), 0.001)
        assertEquals("$a - $b = ${a - b}", a - b, NexusCalculator.operation('-', b, a), 0.001)
        assertEquals("$a / $b = ${a / b}", a / b, NexusCalculator.operation('/', b, a), 0.001)
        assertEquals("$a * $b = ${a * b}", a * b, NexusCalculator.operation('*', b, a), 0.001)
        assertEquals(0.0, NexusCalculator.operation('6', b, a), 0.001)

        //assertEquals("100 / 0 = ${ a / b}",a / b, NexusCalculator.operation('/', 0.0, 100.0), 0.001)
    }
}
