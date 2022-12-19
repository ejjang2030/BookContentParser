package api.naver

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NaverSearchingService {

    /**
     * @author github.com/ejjang2030
     * @param query: 검색어
     * @param display: 한번에 표시할 검색 결과 개수(기본값: 10, 최댓값: 100)
     * @param start: 검색 시작 위치(기본값: 1, 최댓값: 100)
     * @param sort: [검색 결과 정렬 방법] "sim": 정확도순으로 내림차순정렬(기본값) / "date": 출간일순으로 내림차순 정렬
     */
    @GET("v1/search/book.json")
    fun searchBook(
//            @Path("extension") extension: String, // xml 또는 json
            @Query("query") query: String, // 검색어: 기본 UTF-8로 인코딩 되어야 함.
            @Query("display") display: Int? = null, // 한번에 표시할 검색 결과 수(기본값 10, 최댓값 100)
            @Query("start") start: Int? = null, // 겸색 시작위치(기본값 1, 최댓값 1000)
            @Query("sort") sort: String? = null // sim : 정확도순으로 내림차순 정렬(기본값), date : 출간일순으로 내림차순 정렬
    ): Call<BookSearchResult>

    /**
     * @author github.com/ejjang2030
     * @param query: 검색어
     * @param display: 한번에 표시할 검색 결과 개수(기본값: 10, 최댓값: 100)
     * @param start: 검색 시작 위치(기본값: 1, 최댓값: 100)
     * @param sort: [검색 결과 정렬 방법] "sim": 정확도순으로 내림차순정렬(기본값) / "date": 출간일순으로 내림차순 정렬
     * @param d_titl: 검색할 책 제목(책제목과 ISBN은 둘 중 하나라도 있어야 함)
     * @param d_isbn: 검색할 ISBN(책제목과 ISBN은 둘 중 하나라도 있어야 함)
     */
    @GET("v1/search/book_adv.json")
    fun searchBookInDetails(
        @Query("query") query: String? = null,
        @Query("display") display: Int? = null,
        @Query("start") start: Int? = null,
        @Query("sort") sort: String? = null,
        @Query("d_titl") d_titl: String? = null,
        @Query("d_isbn") d_isbn: String? = null
    ): Call<BookSearchResult>
}

interface NaverShoppingSearchingService {
    /**
     * @author github.com/ejjang2030
     * @param catalogNumber: Naver 쇼핑 검색 api에서 book/catalog/{catalogNumber}의 마지막 부분에 들어갈 Number
     */
    @GET("book/catalog/{catalogNumber}")
    fun getBookCatalog(
        @Path("catalogNumber") catalogNumber: String
    ): Call<ResponseBody>
}