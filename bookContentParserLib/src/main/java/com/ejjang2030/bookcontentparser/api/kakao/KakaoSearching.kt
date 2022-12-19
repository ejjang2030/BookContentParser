package com.ejjang2030.bookcontentparser.api.kakao

import api.naver.BookSearchResult
import api.naver.NaverSearching
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class KakaoSearching(
    private val restApiKey: String
) {
    val KAKAO_BASE_URL = "https://dapi.kakao.com/"
    private var retrofit: Retrofit
    private var kakaoSearchService: KakaoSearchingService
    init {
        retrofit = createRetrofitBuilderWithURL(KAKAO_BASE_URL, KakaoInterceptor(restApiKey))
        kakaoSearchService = retrofit.create(KakaoSearchingService::class.java)
    }

    private fun createRetrofitBuilderWithURL(url: String, interceptor: KakaoInterceptor? = null): Retrofit {
        val retrofit = Retrofit.Builder().baseUrl(url)
        if(interceptor != null) {
            retrofit.client(provideOkHttpClient(interceptor))
        }
        return retrofit.addConverterFactory(GsonConverterFactory.create()).build()
    }

    private fun provideOkHttpClient(
        interceptor: KakaoInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .run {
            addInterceptor(interceptor)
            build()
        }

    inner class KakaoInterceptor(private val restApiKey: String) : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain)
                : Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("Authorization", "KakaoAK $restApiKey")
                .build()

            proceed(newRequest)
        }
    }

    private fun <T> getCallback(
        callback: ((Call<T>, retrofit2.Response<T>?, Throwable?) -> Unit)? = null
    ): Callback<T> {
        return object: Callback<T> {
            override fun onResponse(call: Call<T>, response: retrofit2.Response<T>) {
                callback?.invoke(call, response, null)
            }
            override fun onFailure(call: Call<T>, t: Throwable) {
                callback?.invoke(call, null, t)
            }
        }
    }

    /**
     * @author github.com/ejjang2030
     * @param query : 검색을 원하는 질의어
     * @param sort : 결과 문서 정렬 방식, accuracy(정확도순) 또는 latest(발간일순), 기본값 accuracy
     * @param page : 결과 페이지 번호, 1~50 사이의 값, 기본 값 1
     * @param size : 한 페이지에 보여질 문서 수, 1~50 사이의 값, 기본 값 10
     * @param target : [검색 필드 제한] - 사용 가능한 값: title(제목), isbn (ISBN), publisher(출판사), person(인명)
     */
    fun searchBook(
        query: String,
        sort: String? = null,
        page: Int? = null,
        size: Int? = null,
        target: String? = null,
        callback: ((Call<KakaoSearchResult.KakaoBookSearchResult>, retrofit2.Response<KakaoSearchResult.KakaoBookSearchResult>?, Throwable?) -> Unit)? = null
    ) {
        kakaoSearchService.searchBook(query, sort, page, size, target).enqueue(getCallback(callback))
    }
}