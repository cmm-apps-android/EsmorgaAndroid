package cmm.apps.esmorga.view.errors.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Serializable
@Parcelize
data class EsmorgaErrorScreenArguments(
    val isNoInternetError: Boolean = false,
    val title: String,
    val subtitle: String? = null,
    val buttonText: String,
) : Parcelable