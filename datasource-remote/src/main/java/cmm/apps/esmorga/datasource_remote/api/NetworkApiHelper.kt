package cmm.apps.esmorga.datasource_remote.api


import cmm.apps.esmorga.datasource_remote.BuildConfig
import cmm.apps.esmorga.datasource_remote.api.authenticator.EsmorgaAuthenticator
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


class NetworkApiHelper {

    companion object {
        fun esmorgaApiBaseUrl(): String = BuildConfig.ESMORGA_API_BASE_URL
    }

    fun <T> provideApi(
        baseUrl: String,
        clazz: Class<T>,
        authenticator: EsmorgaAuthenticator?,
        authInterceptor: Interceptor?
    ): T {
        val okHttpClient = provideOkHttpClient(authenticator, authInterceptor)
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(clazz)
    }

    private fun provideOkHttpClient(authenticator: EsmorgaAuthenticator?, authInterceptor: Interceptor?): OkHttpClient =
        try {
            OkHttpClient.Builder().apply {
                authenticator?.let {
                    authenticator(it)
                }
                authInterceptor?.let {
                    addInterceptor(it)
                }
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