package cmm.apps.esmorga.datasource_remote.api


import android.util.Log
import okhttp3.Connection
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException


object LogInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        request = interceptRequest(request, chain.connection())
        var response = chain.proceed(request)
        response = interceptResponse(request, response)
        return response
    }

    private fun interceptRequest(request: Request, connection: Connection?): Request {
        Log.v("Interceptor", String.format("Request %s: %s \n%s", request.method, request.url, request.headers.toString()))
        return request
    }

    private fun interceptResponse(request: Request, response: Response): Response {
        return try {
            response.body?.let {
                val responseBodyString = it.string()
                Log.v("Interceptor", String.format("Body: %s\n", responseBodyString))
                response.newBuilder().body(responseBodyString.toResponseBody(it.contentType())).build()
            } ?: response
        } catch (e: IOException) {
            response
        }

    }
}