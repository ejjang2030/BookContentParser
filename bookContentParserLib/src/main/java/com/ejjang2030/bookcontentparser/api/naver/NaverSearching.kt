package api.naver

import com.ejjang2030.bookcontentparser.api.Constants
import com.ejjang2030.bookcontentparser.api.Searching
import okhttp3.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
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


    /**
     * @author github.com/ejjang2030
     * @param bookResult : BookSearchResult안에 items List로 담겨 있는 객체를 하나 선택해서 인자로 전달
     * @param callback : Retrofit Request 요청 성공/실패 시 Retrofit의 enqueue()메서드 안에서 실행될 onResponse(Call, Response)와 onFailure(Call, Throwable)에서 인자를 받아 처리할 람다 함수
     */
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

    /**
     * @author github.com/ejjang2030
     * @param link : BookResult 객체 안에 들어 있는 link 인자를 전달
     * @return String : Naver 쇼핑 검색 api에서 book/catalog/{catalogNumber}의 마지막 부분에 들어갈 Number 가져옴
     */
    fun getCatalogNumber(link: String): String {
        return link.split("/").last()
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
        var pages: Int? = null; var weight: Int? = null; var height: Int? = null; var width: Int? = null; var thickness: Int? = null
        for(spec in bookSpec) {
            val text = spec.text().toString()
            when {
                text.contains("쪽") -> {
                    pages = bookSpec[0].text().toString().replace("쪽", "").toInt()
                }
                text.contains("g") -> {
                    weight= bookSpec[1].text().toString().replace("g", "").toInt()
                }
                text.contains("mm") -> {
                    val triple  = bookSpec[2].text().toString()
                    .replace("mm", "").split("*").map { it.toInt() }
                    width = triple[0]; height = triple[1]; thickness = triple[2]
                }
            }
        }

        val (introduction, publisherComment, contentTable) = body
            .getElementsByClass("infoItem_data_text__bUgVI")
            .map { it.html().toString() }
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
            val ebprice = prices.last()?.getElementsByTag("span")!!.text().toString()
                .replace("구매 ", "")
                .replace("원", "")
                .replace(",", "")
            ebookPrice = if(ebprice.isEmpty()) null else ebprice.toInt()
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