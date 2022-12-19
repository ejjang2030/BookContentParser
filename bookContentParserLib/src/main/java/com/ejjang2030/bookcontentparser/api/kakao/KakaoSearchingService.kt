package com.ejjang2030.bookcontentparser.api.kakao

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface KakaoSearchingService {

    /**
     * @author github.com/ejjang2030
     * @param query : 검색을 원하는 질의어
     * @param sort : 결과 문서 정렬 방식, accuracy(정확도순) 또는 latest(발간일순), 기본값 accuracy
     * @param page : 결과 페이지 번호, 1~50 사이의 값, 기본 값 1
     * @param size : 한 페이지에 보여질 문서 수, 1~50 사이의 값, 기본 값 10
     * @param target : [검색 필드 제한] - 사용 가능한 값: title(제목), isbn (ISBN), publisher(출판사), person(인명)
     */
    @GET("/v3/search/book")
    fun searchBook(
        @Query("query") query: String,
        @Query("sort") sort: String? = null,
        @Query("page") page: Int? = null,
        @Query("size") size: Int? = null,
        @Query("target") target: String? = null
    ): Call<KakaoSearchResult.KakaoBookSearchResult>

}