package cmm.apps.esmorga.view.errors.model

import android.content.Context
import cmm.apps.esmorga.view.R

import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Serializable
data class EsmorgaErrorScreenArguments(
    val animation: Int? = null,
    val title: String,
    val subtitle: String? = null,
    val buttonText: String,
)

object EsmorgaErrorScreenArgumentsHelper : KoinComponent {
    val context: Context by inject()

    fun getEsmorgaDefaultErrorScreenArguments() = EsmorgaErrorScreenArguments(
        title = context.getString(R.string.default_error_title_expanded),
        buttonText = context.getString(R.string.button_retry)
    )

}