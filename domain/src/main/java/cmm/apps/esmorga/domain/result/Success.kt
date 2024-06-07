package cmm.apps.esmorga.domain.result


data class Success<T>(
    val data: T,
    val nonBlockingError: Int = 0
) {
    fun hasError() = nonBlockingError != 0
}
