package api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

object NaverSearching {
    val NAVER_BASE_URL = "https://openapi.naver.com/v1"

    fun getApiClient(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(NAVER_BASE_URL)
                .client(provideOkHttpClient(NaverInterceptor()))
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

    class NaverInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain)
                : Response = with(chain) {
            val newRequest = request().newBuilder()
                    .addHeader("X-Naver-Client-Id", "33chRuAiqlSn5hn8tIme")
                    .addHeader("X-Naver-Client-Secret", "fyfwt9PCUN")
                    .build()

            proceed(newRequest)
        }
    }
}