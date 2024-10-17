package cmm.apps.esmorga.view.errors.model

import kotlinx.serialization.Serializable

@Serializable
data class EsmorgaErrorScreenArguments(
    val animation: Int? = null,
    val title: String,
    val subtitle: String? = null,
    val buttonText: String,
)