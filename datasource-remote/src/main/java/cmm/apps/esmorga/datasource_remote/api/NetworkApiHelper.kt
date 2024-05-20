package cmm.apps.esmorga.datasource_remote.api


import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.Authenticator
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.Date
import java.util.concurrent.TimeUnit


class NetworkApiHelper(private val context: Context) {

    fun <T> provideApi(
        baseUrl: String,
        clazz: Class<T>,
        interceptors: List<Interceptor> = mutableListOf(),
        authenticator: Authenticator? = null,
    ): T =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(provideOkHttpClient(interceptors.toMutableList(), authenticator))
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(clazz)

    private fun provideOkHttpClient(interceptors: MutableList<Interceptor>, authenticator: Authenticator?): OkHttpClient =
        try {
            OkHttpClient.Builder().apply {

                for (interceptor in interceptors) {
                    addInterceptor(interceptor)
                }

                authenticator?.let {
                    authenticator(it)
                }
                val cacheSize: Long = 10 * 1024 * 1024
                cache(Cache(context.cacheDir, cacheSize))

                addInterceptor(CurlLogInterceptor)
                addInterceptor(LogInterceptor)

                connectTimeout(30, TimeUnit.SECONDS)
                readTimeout(30, TimeUnit.SECONDS)
                writeTimeout(30, TimeUnit.SECONDS)
            }.build()
        } catch (e: Exception) {
            throw RuntimeException(e)
        }

}