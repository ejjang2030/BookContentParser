package api.naver

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NaverSearchingService {
    @GET("search/book.json")
    fun searchBook(
//            @Path("extension") extension: String, // xml 또는 json
            @Query("query") query: String, // 검색어: 기본 UTF-8로 인코딩 되어야 함.
            @Query("display") display: Int? = null, // 한번에 표시할 검색 결과 수(기본값 10, 최댓값 100)
            @Query("start") start: Int? = null, // 겸색 시작위치(기본값 1, 최댓값 1000)
            @Query("sort") sort: String? = null // sim : 정확도순으로 내림차순 정렬(기본값), date : 출간일순으로 내림차순 정렬
    ): Call<BookSearchResult>

    @GET("search/book_adv.json")
    fun searchBookInDetails(
        @Query("query") query: String? = null,
        @Query("display") display: Int? = null,
        @Query("start") start: Int? = null,
        @Query("sort") sort: String? = null,
        @Query("d_titl") d_titl: String? = null,
        @Query("d_isbn") d_isbn: String? = null
    ): Call<BookSearchResult>
}