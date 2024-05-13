package cmm.apps.esmorga.datasource_remote.api


import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import java.io.IOException
import java.nio.charset.Charset


object CurlLogInterceptor : Interceptor {

    private val UTF8 = Charset.forName("UTF-8")

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        var compressed = false

        val builder = StringBuilder("curl")
            .append(" -X ")
            .append(request.method)

        val headers = request.headers
        for (i in 0 until headers.size) {
            val name = headers.name(i)
            val value = headers.value(i)
            if ("Accept-Encoding".equals(name, ignoreCase = true) && "gzip".equals(value, ignoreCase = true)) {
                compressed = true
            }
            builder.append(" -H \"").append(name).append(": ").append(value).append("\"")
        }

        request.body?.contentType()?.let {
            builder.append(" -H \"").append("Content-Type").append(": ").append(request.body!!.contentType()!!.toString()).append("\"")
        }

        request.body?.let { requestBody ->
            val buffer = Buffer()
            requestBody.writeTo(buffer)
            var charset: Charset? = UTF8
            val contentType = requestBody.contentType()
            if (contentType != null) {
                charset = contentType.charset(UTF8)
            }
            builder.append(" --data '").append(buffer.readString(charset!!).replace("\n", "\\n")).append("'")
        }

        builder.append(if (compressed) " --compressed " else " ").append(request.url)
        Log.i("cUrl", builder.toString())

        return chain.proceed(request)
    }
}