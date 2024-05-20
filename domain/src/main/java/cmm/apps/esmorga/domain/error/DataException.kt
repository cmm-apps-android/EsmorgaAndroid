package cmm.apps.esmorga.domain.error


data class DataException(
    val code: Int,
    override val message: String
) : RuntimeException()
