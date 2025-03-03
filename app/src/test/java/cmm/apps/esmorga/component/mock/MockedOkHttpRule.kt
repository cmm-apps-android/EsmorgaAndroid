package cmm.apps.esmorga.component.mock

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.ExternalResource
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class MockedOkHttpRule<T : Any>(
    private val clazz: Class<T>,
    private val testDispatcher: CoroutineDispatcher = StandardTestDispatcher(),
    private val okHttpClient: OkHttpClient = OkHttpClient.Builder().build(),
    private val gson: Gson = Gson()
) : ExternalResource() {
    private val server: MockWebServer = MockWebServer()
    lateinit var apiService: T
    private lateinit var retrofit: Retrofit
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun before() {
        Dispatchers.setMain(testDispatcher)
        try {
            server.start()

            retrofit = Retrofit.Builder()
                .baseUrl(server.url("/"))
                .client(okHttpClient)
                .callFactory(
                    TestCallFactory(
                        delegate = okHttpClient,
                        testDispatcher = testDispatcher
                    )
                )
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
            apiService = retrofit.create(clazz)
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun after() {
        Dispatchers.resetMain()
        try {
            server.shutdown()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun enqueue(block: () -> MockResponse) {
        server.enqueue(block())
    }
}

fun mockResponseSuccess(data: String): MockResponse = MockResponse()
    .setBody(data)
    .setResponseCode(200)

fun Any.loadRawFile(fileName: String): String {
    return javaClass.getResourceAsStream(fileName)?.bufferedReader()
        .use { it?.readText() } ?: ""
}

