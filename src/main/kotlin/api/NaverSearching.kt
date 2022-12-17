package api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object NaverSearching {
    val NAVER_BASE_URL = "https://openapi.naver.com/v1/"

    fun getApiClient(clientId: String, clientIdSecret: String): Retrofit {
        return Retrofit.Builder()
                .baseUrl(NAVER_BASE_URL)
                .client(provideOkHttpClient(NaverInterceptor(clientId, clientIdSecret)))
                .addConverterFactory(GsonConverterFactory.create())
                .build()
    }

    private fun provideOkHttpClient(
            interceptor: NaverInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
            .run {
                addInterceptor(interceptor)
                build()
            }

    class NaverInterceptor(private val clientId: String, private val clientIdSecret: String) : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain)
                : Response = with(chain) {
            val newRequest = request().newBuilder()
                    .addHeader("X-Naver-Client-Id", clientId)
                    .addHeader("X-Naver-Client-Secret", clientIdSecret)
                    .build()

            proceed(newRequest)
        }
    }
}