package com.ejjang2030.bookcontentparser.api.kakao

import com.google.gson.annotations.SerializedName

sealed class KakaoSearchResult {
    data class KakaoBookSearchResult(
        @SerializedName("meta") val meta: KakaoSearchMetaResult,
        @SerializedName("documents") val documents: List<KakaoSearchDocsResult.KakaoSearchDocsBookResult>
    ): KakaoSearchResult()
    data class KakaoCafeSearchResult(
        @SerializedName("meta") val meta: KakaoSearchMetaResult,
        @SerializedName("documents") val documents: List<KakaoSearchDocsResult.KakaoSearchDocsCafeResult>
    ): KakaoSearchResult()
    data class KakaoBlogSearchResult(
        @SerializedName("meta") val meta: KakaoSearchMetaResult,
        @SerializedName("documents") val documents: List<KakaoSearchDocsResult.KakaoSearchDocsBlogResult>
    ): KakaoSearchResult()
    data class KakaoImageSearchResult(
        @SerializedName("meta") val meta: KakaoSearchMetaResult,
        @SerializedName("documents") val documents: List<KakaoSearchDocsResult.KakaoSearchDocsImageResult>
    ): KakaoSearchResult()
    data class KakaoVideosSearchResult(
        @SerializedName("meta") val meta: KakaoSearchMetaResult,
        @SerializedName("documents") val documents: List<KakaoSearchDocsResult.KakaoSearchDocsVideosResult>
    ): KakaoSearchResult()
    data class KakaoWebSearchResult(
        @SerializedName("meta") val meta: KakaoSearchMetaResult,
        @SerializedName("documents") val documents: List<KakaoSearchDocsResult.KakaoSearchDocsWebResult>
    ): KakaoSearchResult()
}

data class KakaoSearchMetaResult(
    @SerializedName("total_count") val total_count: Int,
    @SerializedName("pageable_count") val pageable_count: Int,
    @SerializedName("is_end") val is_end: Boolean
)

sealed class KakaoSearchDocsResult {
    data class KakaoSearchDocsBookResult(
        @SerializedName("authors") val authors: List<String>,
        @SerializedName("contents") val contents: String,
        @SerializedName("datetime") val datetime: String,
        @SerializedName("isbn") val isbn: String,
        @SerializedName("price") val price: Int,
        @SerializedName("publisher") val publisher: String,
        @SerializedName("sale_price") val sale_price: Int,
        @SerializedName("status") val status: String,
        @SerializedName("thumbnail") val thumbnail: String,
        @SerializedName("title") val title: String,
        @SerializedName("translators") val translators: List<String>,
        @SerializedName("url") val url: String
    ): KakaoSearchDocsResult()
    data class KakaoSearchDocsCafeResult(
        @SerializedName("title") val title: String,
        @SerializedName("contents") val contents: String,
        @SerializedName("url") val url: String,
        @SerializedName("cafename") val cafename: String,
        @SerializedName("thumbnail") val thumbnail: String,
        @SerializedName("datetime") val datetime: String,
    ): KakaoSearchDocsResult()
    data class KakaoSearchDocsBlogResult(
        @SerializedName("title") val title: String,
        @SerializedName("contents") val contents: String,
        @SerializedName("url") val url: String,
        @SerializedName("blogname") val blogname: String,
        @SerializedName("thumbnail") val thumbnail: String,
        @SerializedName("datetime") val datetime: String
    ): KakaoSearchDocsResult()
    data class KakaoSearchDocsImageResult(
        @SerializedName("collection") val collection: String,
        @SerializedName("thumbnail_url") val thumbnail_url: String,
        @SerializedName("image_url") val image_url: String,
        @SerializedName("width") val width: Int,
        @SerializedName("height") val height: Int,
        @SerializedName("display_sitename") val display_sitename: String,
        @SerializedName("doc_url") val doc_url: String,
        @SerializedName("datetime") val datetime: String
    ): KakaoSearchDocsResult()
    data class KakaoSearchDocsVideosResult(
        @SerializedName("title") val title: String,
        @SerializedName("url") val url: String,
        @SerializedName("datetime") val datetime: String,
        @SerializedName("play_time") val play_time: Int,
        @SerializedName("thumbnail") val thumbnail: String,
        @SerializedName("author") val author: String
    ): KakaoSearchDocsResult()
    data class KakaoSearchDocsWebResult(
        @SerializedName("title") val title: String,
        @SerializedName("contents") val contents: String,
        @SerializedName("url") val url: String,
        @SerializedName("datetime") val datetime: String
    ): KakaoSearchDocsResult()
}