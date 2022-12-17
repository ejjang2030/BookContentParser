package api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.lang.NullPointerException
import java.util.*

class NaverSearching(val clientId: String, val clientIdSecret: String) {
    val NAVER_BASE_URL = "https://openapi.naver.com/v1/"
    private var retrofit: Retrofit
    private var service: NaverSearchingService
    private var bookSearchResult: BookSearchResult? = null
    init {
        retrofit = Retrofit.Builder()
            .baseUrl(NAVER_BASE_URL)
            .client(provideOkHttpClient(NaverInterceptor(clientId, clientIdSecret)))
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        service = retrofit.create(NaverSearchingService::class.java)
    }

    fun getInstance(): Retrofit {
        return retrofit
    }

    fun getService(): NaverSearchingService {
        return service
    }

    private fun provideOkHttpClient(
        interceptor: NaverInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .run {
            addInterceptor(interceptor)
            build()
        }

    inner class NaverInterceptor(private val clientId: String, private val clientIdSecret: String) : Interceptor {
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

    /**
     * @author github.com/ejjang2030
     * @param query: 검색어
     * @param display: 한번에 표시할 검색 결과 개수(기본값: 10, 최댓값: 100)
     * @param start: 검색 시작 위치(기본값: 1, 최댓값: 100)
     * @param sort: [검색 결과 정렬 방법] "sim": 정확도순으로 내림차순정렬(기본값) / "date": 출간일순으로 내림차순 정렬
     * @return {BookSearchResult}
     */
    fun searchBook(
        query: String,
        display: Int? = null,
        start: Int? = null,
        sort: String? = null
    ): BookSearchResult? {
        var searchResult: BookSearchResult? = null
        service.searchBook(query, display, start, sort).enqueue(object: Callback<BookSearchResult> {
            override fun onResponse(call: Call<BookSearchResult>, response: retrofit2.Response<BookSearchResult>) {
                if(response.isSuccessful) {
                    val result = response.body()
                    println(result)
                    if (result != null) {
                        searchResult = result.copy()
                    }
                } else {
                    println(response.message())
                }
            }
            override fun onFailure(call: Call<BookSearchResult>, t: Throwable) {
                println(t.message)
            }
        })
        return searchResult
    }

    /**
     * @author github.com/ejjang2030
     * @param query: 검색어
     * @param display: 한번에 표시할 검색 결과 개수(기본값: 10, 최댓값: 100)
     * @param start: 검색 시작 위치(기본값: 1, 최댓값: 100)
     * @param sort: [검색 결과 정렬 방법] "sim": 정확도순으로 내림차순정렬(기본값) / "date": 출간일순으로 내림차순 정렬
     * @param d_titl: 검색할 책 제목(책제목과 ISBN은 둘 중 하나라도 있어야 함)
     * @param d_isbn: 검색할 ISBN(책제목과 ISBN은 둘 중 하나라도 있어야 함)
     * @exception NullPointerException : d_titl 또는 d_isbn 둘 중 하나라도 없을 시 발생
     * @return
     */
    fun searchBookInDetails(
        query: String? = null, // 검색어
        display: Int? = null, // 한번에 표시할 검색 결과 개수(기본값: 10, 최댓값: 100)
        start: Int? = null, // 검색 시작 위치(기본값: 1, 최댓값: 100)
        sort: String? = null, // [검색 결과 정렬 방법] "sim": 정확도순으로 내림차순정렬(기본값) / "date": 출간일순으로 내림차순 정렬
        d_titl: String? = null, // 검색할 책 제목(책제목과 ISBN은 둘 중 하나라도 있어야 함)
        d_isbn: String? = null // 검색할 ISBN(책제목과 ISBN은 둘 중 하나라도 있어야 함)
    ): NaverSearching {
        if(d_titl == null && d_isbn == null) {
            throw NullPointerException("Either 'd_titl' or 'd_isbn' must not be null or blank")
        }
        service.searchBookInDetails(query, display, start, sort, d_titl, d_isbn).enqueue(object: Callback<BookSearchResult> {
            override fun onResponse(call: Call<BookSearchResult>, response: retrofit2.Response<BookSearchResult>) {
                if(response.isSuccessful) {
                    this@NaverSearching.setBookSearchResult(response.body()!!)
                } else {
                    println(response.message())
                }
            }
            override fun onFailure(call: Call<BookSearchResult>, t: Throwable) {
                println(t.message)
            }
        })
        return this
    }

    private fun setBookSearchResult(bookSearchResult: BookSearchResult) {
        this.bookSearchResult = bookSearchResult
    }

    fun getBookSearhResult(): BookSearchResult? {
        return this.bookSearchResult
    }
}