package api

import okhttp3.*
import okio.Timeout
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException


class NaverSearching(val clientId: String, val clientIdSecret: String) {
    val NAVER_BASE_URL = "https://openapi.naver.com/v1/"
    val NAVER_SHOPPING_SEARCHING_URL = "https://search.shopping.naver.com/"
    private var retrofit: Retrofit
    private var shoppingRetrofit: Retrofit
    private var naverSearchingService: NaverSearchingService
    private var naverShoppingSearchingService: NaverShoppingSearchingService

    init {
        retrofit = createRetrofitBuilderWithURL(NAVER_BASE_URL, NaverInterceptor(clientId, clientIdSecret))
        shoppingRetrofit = createRetrofitBuilderWithURL(NAVER_SHOPPING_SEARCHING_URL)
        naverSearchingService = retrofit.create(NaverSearchingService::class.java)
        naverShoppingSearchingService = shoppingRetrofit.create(NaverShoppingSearchingService::class.java)
    }

    private fun createRetrofitBuilderWithURL(url: String, naverInterceptor: NaverInterceptor? = null): Retrofit {
        val retrofit = Retrofit.Builder().baseUrl(url)
        if(naverInterceptor != null) {
            retrofit.client(provideOkHttpClient(naverInterceptor))
        }
        return retrofit.addConverterFactory(GsonConverterFactory.create()).build()
    }

    fun getRetrofit(): Retrofit {
        return retrofit
    }

    fun getNaverSearchingService(): NaverSearchingService {
        return naverSearchingService
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
     * @exception NullPointerException : d_titl 또는 d_isbn 둘 중 하나라도 없을 시 발생
     * @return
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

    private fun getCatalogNumber(naverShoppingSearchingUri: String): String {
        return naverShoppingSearchingUri.split("/").last()
    }

    fun getBookCatalog(
            bookResult: BookResult,
            callback: ((Call<ResponseBody>, retrofit2.Response<ResponseBody>?, BookCatalogResult?, Throwable?) -> Unit)? = null
    ) {
            val number: String = getCatalogNumber(bookResult.link)
            naverShoppingSearchingService.getBookCatalog(number).enqueue(object: Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: retrofit2.Response<ResponseBody>) {
                    if(response.isSuccessful) {
                        val html = response.body()!!.string()
                        val result = createBookCatalogResult(bookResult, number, html)
                        callback?.invoke(call, response, result, null)
                    } else {
                        callback?.invoke(call, response, null, null)
                    }
                }
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    callback?.invoke(call, null, null, t)
                }
            })
        }

    private fun createBookCatalogResult(
            bookResult: BookResult,
            catalogNumber: String,
            html: String
    ): BookCatalogResult {
        val document: Document = Jsoup.parse(html)
        val body = document.body()
        val bookInfoDetail = body.getElementsByClass("bookBasicInfo_info_detail__I0Fx5")
        val category: String = bookInfoDetail.first()?.text().toString()
        val bookSpec = bookInfoDetail[1].getElementsByClass("bookBasicInfo_spec__qmQ_N")
        val pages: Int = bookSpec[0].text().toString().replace("쪽", "").toInt()
        val weight: Int = bookSpec[1].text().toString().replace("g", "").toInt()
        val (width, height, thickness) = bookSpec[2].text().toString()
                .replace("mm", "").split("*").map { it.toInt() }
        val (introduction, publisherComment, contentTable) = body
                .getElementsByClass("infoItem_data_text__bUgVI")
                .map { it.text().toString() }
        val grade = body.getElementsByClass("bookReview_grade__0kV7o")
                .text().replace("평점", "")
        val starRate = if(grade.isEmpty()) null else grade.toDouble()
        val innerContent = body.getElementsByClass("bookTitle_inner_content__REoK1")
        val ranking: String? = if(innerContent.size >= 4) innerContent[3].text().toString() else null
        val prices = body.getElementsByClass("bookSeller_price__opK_s")
        val price = prices[0]
                .text().toString()
                .replace("최저 ", "")
                .replace("원", "")
                .replace(",", "").toInt()
        var ebookPrice: Int? = null
        if(prices.last()?.getElementsByTag("span") != null) {
            ebookPrice = prices.last()?.getElementsByTag("span")!!.text().toString()
                    .replace("구매 ", "")
                    .replace("원", "")
                    .replace(",", "").toInt()
        }
        val authorIntroduction = body.getElementsByClass("authorIntroduction_introduce_text__RYZDj")
                .text().toString()
        val mainTitle = body.getElementsByClass("bookTitle_book_name__JuBQ2").text().toString()
        val sub = body.getElementsByClass("bookTitle_sub_title__B0uMS").text()
        var subTitle: String? = null
        if(!sub.isEmpty()) {
            subTitle = sub.toString()
        }
        return BookCatalogResult(
                category = category,
                catalogNumber = catalogNumber,
                spec = BookSpec(pages, weight, width, height, thickness),
                descriptions = BookDescriptions(introduction, publisherComment, contentTable, authorIntroduction),
                evaluations = BookEvaluations(starRate, ranking),
                price = price,
                ebookPrice = ebookPrice,
                mainTitle = mainTitle,
                subTitle = subTitle,
                bookResult = bookResult
        )
    }
}