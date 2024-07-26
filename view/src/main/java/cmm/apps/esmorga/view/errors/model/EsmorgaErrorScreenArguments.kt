package cmm.apps.esmorga.view.errors.model

import android.os.Parcelable
import cmm.apps.esmorga.view.navigation.serializableType
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlin.reflect.typeOf

@Serializable
@Parcelize
data class EsmorgaErrorScreenArguments(
    val title: String,
    val buttonText: String,
) : Parcelable {
    companion object {
        val typeMap = mapOf(typeOf<EsmorgaErrorScreenArguments>() to serializableType<EsmorgaErrorScreenArguments>())
    }
}