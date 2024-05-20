package cmm.apps.esmorga.data.error


data class RemoteHttpException(
    val code: Int,
    override val message: String
) : RuntimeException()