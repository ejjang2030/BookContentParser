package com.ejjang2030.bookcontentparser.api.kakao

import com.ejjang2030.bookcontentparser.api.Constants
import com.ejjang2030.bookcontentparser.api.Searching
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class KakaoSearching(
    private val restApiKey: String
): Searching() {
    private var retrofit: Retrofit = createRetrofitBuilderWithURL(Constants.KAKAO_BASE_URL, KakaoInterceptor(restApiKey))
    private var kakaoSearchService: KakaoSearchingService = retrofit.create(KakaoSearchingService::class.java)

    /**
     * @author github.com/ejjang2030
     * @param query : 검색을 원하는 질의어
     * @param sort : 결과 문서 정렬 방식, accuracy(정확도순) 또는 latest(발간일순), 기본값 accuracy
     * @param page : 결과 페이지 번호, 1~50 사이의 값, 기본 값 1
     * @param size : 한 페이지에 보여질 문서 수, 1~50 사이의 값, 기본 값 10
     * @param target : [검색 필드 제한] - 사용 가능한 값: title(제목), isbn (ISBN), publisher(출판사), person(인명)
     * @param callback : Retrofit Request 요청 성공/실패 시 Retrofit의 enqueue()메서드 안에서 실행될 onResponse(Call, Response)와 onFailure(Call, Throwable)에서 인자를 받아 처리할 람다 함수
     */
    @JvmOverloads
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

    private inner class KakaoInterceptor(private val restApiKey: String) : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain)
                : Response = with(chain) {
            val newRequest = request().newBuilder()
                .addHeader("Authorization", "KakaoAK $restApiKey")
                .build()

            proceed(newRequest)
        }
    }
}