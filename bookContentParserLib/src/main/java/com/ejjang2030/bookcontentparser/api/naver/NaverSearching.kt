package api.naver

import com.ejjang2030.bookcontentparser.api.Constants
import com.ejjang2030.bookcontentparser.api.Searching
import com.ejjang2030.bookcontentparser.api.naver.BookCatalog
import com.ejjang2030.bookcontentparser.api.naver.BookCatalogBuilder
import okhttp3.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import java.io.IOException

class NaverSearching(
    private val clientId: String,
    private val clientIdSecret: String
    ): Searching() {

    private var retrofit: Retrofit =
        createRetrofitBuilderWithURL(Constants.NAVER_BASE_URL, NaverInterceptor(clientId, clientIdSecret))
    private var shoppingRetrofit: Retrofit = createRetrofitBuilderWithURL(Constants.NAVER_SHOPPING_SEARCHING_URL)
    private var naverSearchingService: NaverSearchingService = retrofit.create(NaverSearchingService::class.java)
    private var naverShoppingSearchingService: NaverShoppingSearchingService = shoppingRetrofit.create(NaverShoppingSearchingService::class.java)


    /**
     * @author github.com/ejjang2030
     * @param bookResult : BookResult 타입, BookSearchResult안에 items List로 담겨 있는 객체를 하나 선택해서 인자로 전달
     * @param callback : ((BookResult, BookCatalog?, Call<ResponseBody>, retrofit2.Response<ResponseBody>?, Throwable?) -> Unit)? = null
     */
    fun getBookCatalog(
        bookResult: BookResult,
        callback: ((BookResult, BookCatalog?, Call<ResponseBody>, retrofit2.Response<ResponseBody>?, Throwable?) -> Unit)? = null
    ) {
        val number: String = getCatalogNumber(bookResult.link)
        naverShoppingSearchingService.getBookCatalog(number).enqueue(object: Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                if(response.isSuccessful) {
                    val html = response.body()!!.string()
                    val result = createBookCatalog(html)
                    callback?.invoke(bookResult, result, call, response, null)
                } else {
                    callback?.invoke(bookResult, null, call, response, null)
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                callback?.invoke(bookResult, null, call, null, t)
            }
        })
    }


    /**
     * @author github.com/ejjang2030
     * @param query: 검색어
     * @param display: 한번에 표시할 검색 결과 개수(기본값: 10, 최댓값: 100)
     * @param start: 검색 시작 위치(기본값: 1, 최댓값: 100)
     * @param sort: [검색 결과 정렬 방법] "sim": 정확도순으로 내림차순정렬(기본값) / "date": 출간일순으로 내림차순 정렬
     * @param callback : Retrofit Request 요청 성공/실패 시 Retrofit의 enqueue()메서드 안에서 실행될 onResponse(Call, Response)와 onFailure(Call, Throwable)에서 인자를 받아 처리할 람다 함수
     */
    @JvmOverloads
    fun searchBook(
        query: String,
        display: Int? = null,
        start: Int? = null,
        sort: String? = null,
        callback: ((Call<BookSearchResult>, retrofit2.Response<BookSearchResult>?, Throwable?) -> Unit)? = null
    ) {
        naverSearchingService.searchBook(query, display, start, sort).enqueue(getCallback(callback))
    }

    /**
     * @author github.com/ejjang2030
     * @param query: 검색어
     * @param display: 한번에 표시할 검색 결과 개수(기본값: 10, 최댓값: 100)
     * @param start: 검색 시작 위치(기본값: 1, 최댓값: 100)
     * @param sort: [검색 결과 정렬 방법] "sim": 정확도순으로 내림차순정렬(기본값) / "date": 출간일순으로 내림차순 정렬
     * @param d_titl: 검색할 책 제목(책제목과 ISBN은 둘 중 하나라도 있어야 함)
     * @param d_isbn: 검색할 ISBN(책제목과 ISBN은 둘 중 하나라도 있어야 함)
     * @param callback : Retrofit Request 요청 성공/실패 시 Retrofit의 enqueue()메서드 안에서 실행될 onResponse(Call, Response)와 onFailure(Call, Throwable)에서 인자를 받아 처리할 람다 함수
     * @exception NullPointerException : d_titl 또는 d_isbn 둘 중 하나라도 없을 시 발생
     */
    fun searchBookInDetails(
        query: String? = null, // 검색어
        display: Int? = null, // 한번에 표시할 검색 결과 개수(기본값: 10, 최댓값: 100)
        start: Int? = null, // 검색 시작 위치(기본값: 1, 최댓값: 100)
        sort: String? = null, // [검색 결과 정렬 방법] "sim": 정확도순으로 내림차순정렬(기본값) / "date": 출간일순으로 내림차순 정렬
        d_titl: String? = null, // 검색할 책 제목(책제목과 ISBN은 둘 중 하나라도 있어야 함)
        d_isbn: String? = null, // 검색할 ISBN(책제목과 ISBN은 둘 중 하나라도 있어야 함)
        callback: ((Call<BookSearchResult>, retrofit2.Response<BookSearchResult>?, Throwable?) -> Unit)? = null
    ) {
        if(d_titl == null && d_isbn == null) {
            throw NullPointerException("Either 'd_titl' or 'd_isbn' must not be null or blank")
        }
        naverSearchingService.searchBookInDetails(query, display, start, sort, d_titl, d_isbn).enqueue(getCallback(callback))
    }

    private fun getCatalogNumber(link: String): String {
        return link.split("/").last()
    }

    private fun createBookCatalog(html: String): BookCatalog? {
        return BookCatalogBuilder.createBookCatalogFromHtmlString(html)
    }

    private inner class NaverInterceptor(private val clientId: String, private val clientIdSecret: String) : Interceptor {
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