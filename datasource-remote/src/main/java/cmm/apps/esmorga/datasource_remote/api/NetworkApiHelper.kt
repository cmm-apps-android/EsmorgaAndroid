package cmm.apps.esmorga.datasource_remote.api


import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


class NetworkApiHelper() {

    private val okHttpClient = provideOkHttpClient()

    fun <T> provideApi(
        baseUrl: String,
        clazz: Class<T>
    ): T =
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(clazz)

    private fun provideOkHttpClient(): OkHttpClient =
        try {
            OkHttpClient.Builder().apply {
                //TODO add authenticator when needed
//              authenticator(oauthAuthenticator)

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