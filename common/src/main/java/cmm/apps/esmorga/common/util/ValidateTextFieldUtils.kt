package cmm.apps.esmorga.common.util

object ValidateTextFieldUtils {

    fun getFieldErrorText(
        value: String,
        errorTextProvider: String,
        errorTextEmpty: String,
        acceptsEmpty: Boolean,
        nonEmptyCondition: Boolean
    ): String? {
        val isBlank = value.isBlank()
        val isValid = value.isEmpty() || nonEmptyCondition

        return when {
            !acceptsEmpty && isBlank -> errorTextEmpty
            !isValid -> errorTextProvider
            else -> null
        }
    }
}