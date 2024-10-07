package cmm.apps.esmorga.domain.result

data class EsmorgaResult<T>(
    val data: T? = null,
    val error: EsmorgaException? = null
) {
    companion object {
        fun <T> success(value: T): EsmorgaResult<T> =
            EsmorgaResult(value)

        fun <T> failure(exception: Throwable): EsmorgaResult<T> {
            val error: EsmorgaException = if(exception is EsmorgaException){
                exception
            } else {
                EsmorgaException(message = "Unknown error: ${exception.message}", code = ErrorCodes.UNKNOWN_ERROR, source = Source.UNSUPPORTED)
            }
            return EsmorgaResult(error = error)
        }

        fun <T> noConnectionError(value: T): EsmorgaResult<T> =
            EsmorgaResult(data = value, error = EsmorgaException(message = "No Connection", code = ErrorCodes.NO_CONNECTION, source = Source.REMOTE))
    }

    fun onSuccess(action: (value: T) -> Unit): EsmorgaResult<T> {
        if(data != null){
            action(data)
        }
        return this
    }

    fun onFailure(action: (exception: EsmorgaException) -> Unit): EsmorgaResult<T> {
        if(data == null && error != null){
            action(error)
        }
        return this
    }

    fun onNoConnectionError(action: () -> Unit): EsmorgaResult<T> {
        if(error != null && error.code == ErrorCodes.NO_CONNECTION){
            action()
        }
        return this
    }
}