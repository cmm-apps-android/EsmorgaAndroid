package cmm.apps.esmorga.domain.createEvent

interface ValidateCreateEventFormUseCase {
    operator fun invoke(
        name: String,
        description: String,
        finalValidation: Boolean,
        emptyFieldError: Int,
        invalidNameError: Int,
        invalidDescriptionError: Int
    ): CreateEventValidationResult
}

class ValidateCreateEventFormUseCaseImpl : ValidateCreateEventFormUseCase {
    override fun invoke(
        name: String,
        description: String,
        finalValidation: Boolean,
        emptyFieldError: Int,
        invalidNameError: Int,
        invalidDescriptionError: Int
    ): CreateEventValidationResult {
        val nameValid = name.length in 3..100
        val descValid = description.length in 4..5000

        val nameError = if (finalValidation && name.isBlank()) {
            emptyFieldError
        } else if (name.isNotBlank() && !nameValid) {
            invalidNameError
        } else null

        val descError = if (finalValidation && description.isBlank()) {
            emptyFieldError
        } else if (description.isNotBlank() && !descValid) {
            invalidDescriptionError
        } else null

        return CreateEventValidationResult(
            nameErrorRes = nameError,
            descriptionErrorRes = descError,
            isFormValid = nameValid && descValid
        )
    }
}

data class CreateEventValidationResult(
    val nameErrorRes: Int? = null,
    val descriptionErrorRes: Int? = null,
    val isFormValid: Boolean = false
)
