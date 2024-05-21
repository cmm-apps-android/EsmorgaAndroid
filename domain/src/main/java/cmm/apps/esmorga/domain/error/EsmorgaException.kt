package cmm.apps.esmorga.domain.error

enum class Source {
    REMOTE,
    LOCAL,
    UNSUPPORTED
}

class EsmorgaException(
    override val message: String,
    val source: Source?,
    val code: Int?
) : RuntimeException()