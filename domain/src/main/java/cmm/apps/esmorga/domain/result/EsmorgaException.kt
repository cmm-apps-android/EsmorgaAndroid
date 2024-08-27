package cmm.apps.esmorga.domain.result

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

object ErrorCodes {

    const val UNKNOWN_ERROR = -1
    const val UNSUPPORTED_OPERATION = -10
    const val PARSE_ERROR = -100
    const val NO_DATA = -404
    const val NO_CONNECTION = -999

}

