package cmm.apps.esmorga.common.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ValidateTextFieldUtilsTest {

    private val errorTextProvider = "Error: Invalid input"
    private val errorTextEmpty = "Error: Field cannot be empty"

    @Test
    fun `getFieldErrorText returns errorTextEmpty when value is blank and acceptsEmpty is false`() {
        val result = ValidateTextFieldUtils.getFieldErrorText(
            value = "   ",
            errorTextProvider = errorTextProvider,
            errorTextEmpty = errorTextEmpty,
            acceptsEmpty = false,
            nonEmptyCondition = true
        )
        assertEquals(errorTextEmpty, result)
    }

    @Test
    fun `getFieldErrorText returns errorTextProvider when value is not empty and nonEmptyCondition is false`() {
        val result = ValidateTextFieldUtils.getFieldErrorText(
            value = "someValue",
            errorTextProvider = errorTextProvider,
            errorTextEmpty = errorTextEmpty,
            acceptsEmpty = true,
            nonEmptyCondition = false
        )
        assertEquals(errorTextProvider, result)
    }

    @Test
    fun `getFieldErrorText returns null when value is empty and acceptsEmpty is true`() {
        val result = ValidateTextFieldUtils.getFieldErrorText(
            value = "",
            errorTextProvider = errorTextProvider,
            errorTextEmpty = errorTextEmpty,
            acceptsEmpty = true,
            nonEmptyCondition = true
        )
        assertNull(result)
    }

    @Test
    fun `getFieldErrorText returns null when value is not empty, nonEmptyCondition is true and acceptsEmpty is true`() {
        val result = ValidateTextFieldUtils.getFieldErrorText(
            value = "validValue",
            errorTextProvider = errorTextProvider,
            errorTextEmpty = errorTextEmpty,
            acceptsEmpty = true,
            nonEmptyCondition = true
        )
        assertNull(result)
    }

    @Test
    fun `getFieldErrorText returns null when value is not empty, nonEmptyCondition is true and acceptsEmpty is false`() {

        val result = ValidateTextFieldUtils.getFieldErrorText(
            value = "validValue",
            errorTextProvider = errorTextProvider,
            errorTextEmpty = errorTextEmpty,
            acceptsEmpty = false,
            nonEmptyCondition = true
        )
        assertNull(result)
    }

    @Test
    fun `getFieldErrorText returns errorTextProvider when value is empty, acceptsEmpty is false and nonEmptyCondition is false`() {
        val result = ValidateTextFieldUtils.getFieldErrorText(
            value = "",
            errorTextProvider = errorTextProvider,
            errorTextEmpty = errorTextEmpty,
            acceptsEmpty = false,
            nonEmptyCondition = false
        )
        assertEquals(errorTextEmpty, result)
    }

    @Test
    fun `getFieldErrorText returns errorTextProvider when value is not empty, acceptsEmpty is true and nonEmptyCondition is false`() {
        val result = ValidateTextFieldUtils.getFieldErrorText(
            value = "abc",
            errorTextProvider = errorTextProvider,
            errorTextEmpty = errorTextEmpty,
            acceptsEmpty = true,
            nonEmptyCondition = false
        )
        assertEquals(errorTextProvider, result)
    }

    @Test
    fun `getFieldErrorText returns null when value is empty, acceptsEmpty is true and nonEmptyCondition is false`() {
        val result = ValidateTextFieldUtils.getFieldErrorText(
            value = "",
            errorTextProvider = errorTextProvider,
            errorTextEmpty = errorTextEmpty,
            acceptsEmpty = true,
            nonEmptyCondition = false
        )
       assertNull(result)
    }
}