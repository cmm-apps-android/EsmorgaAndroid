package cmm.apps.esmorga.component.mock

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okio.Timeout
import java.io.IOException

class TestCallFactory(
    private val delegate: OkHttpClient,
    private val testDispatcher: CoroutineDispatcher
) : Call.Factory {

    override fun newCall(request: Request): Call {
        return object : Call {
            private var executed = false
            private var canceled = false
            private val currentCall: Call = this

            override fun execute(): Response = runBlocking(testDispatcher) {
                executed = true
                delegate.newCall(request).execute()
            }

            override fun enqueue(responseCallback: Callback) {
                executed = true
                CoroutineScope(testDispatcher).launch {
                    try {
                        val response = delegate.newCall(request).execute()
                        responseCallback.onResponse(currentCall, response)
                    } catch (e: IOException) {
                        responseCallback.onFailure(currentCall, e)
                    }
                }
            }

            override fun timeout(): Timeout = Timeout.NONE
            override fun isExecuted(): Boolean = executed
            override fun cancel() {
                canceled = true
            }

            override fun isCanceled(): Boolean = canceled
            override fun clone(): Call = delegate.newCall(request)
            override fun request(): Request = request
        }
    }
}