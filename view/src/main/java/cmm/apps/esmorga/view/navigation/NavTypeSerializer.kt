package cmm.apps.esmorga.view.navigation

import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

inline fun <reified T : Any> serializableType(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String): T? = bundle.getString(key)?.let { parseValue(it) }

    override fun parseValue(value: String): T = json.decodeFromString(URLDecoder.decode(value, StandardCharsets.UTF_8.toString()))

    override fun serializeAsValue(value: T): String = URLEncoder.encode(json.encodeToString(value), StandardCharsets.UTF_8.toString())

    override fun put(bundle: Bundle, key: String, value: T) {
        bundle.putString(key, json.encodeToString(value))
    }
}