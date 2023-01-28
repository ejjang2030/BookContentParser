package com.ejjang2030.bookcontentparser.api.naver

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import retrofit2.Call
import retrofit2.Callback


object BookCatalogBuilder {
    fun createBookCatalogFromHtmlString(html: String): BookCatalog? {
        val document: Document = Jsoup.parse(html)
        val body = document.body()
        val nextData = body.getElementById("__NEXT_DATA__")
        if(nextData != null) {
            val parser = JsonParser();
            val json = parser.parse(nextData.data()).asJsonObject
            val props = json["props"].asJsonObject
            val pageProps = props["pageProps"].asJsonObject
            val dehydratedState = pageProps["dehydratedState"].asJsonObject
            val queries = dehydratedState["queries"].asJsonArray
            val state = queries[0].asJsonObject.get("state").asJsonObject
            val data = state["data"].asJsonObject
            val BookCatalog = data["BookCatalog"].asJsonObject
            return getBookCatalogFromJsonObject(BookCatalog)
        }
        return null
    }
    fun getBookCatalogFromJsonObject(jsonObject: JsonObject): BookCatalog {
        val nvMid = jsonObject["nvMid"].asString
        val categoryId = jsonObject["categoryId"].asString
        val categoryName = jsonObject["categoryName"].asString
        val fullCategoryId = jsonObject["fullCategoryId"].asString
        val fullCategoryName = jsonObject["fullCategoryName"].asString
        val title = jsonObject["title"].asString
        val subtitle = jsonObject["subtitle"].asString
        val publisher = jsonObject["publisher"].asString
        val publishDay = jsonObject["publishDay"].asString
        val isNew = jsonObject["isNew"].asBoolean
        val isBestseller = jsonObject["isBestseller"].asBoolean
        val isAdult = jsonObject["isAdult"].asBoolean
        val thumbnailUrl = jsonObject["thumbnailUrl"].asString
        val authorList = jsonObject["authorList"].asJsonArray.map { it.asJsonObject["name"].asString }.toList()
//        val illustratorList = jsonObject["illustratorList"].asJsonArray
//        val translatorList = jsonObject["translatorList"].asJsonArray
        val bestsellerRanking = jsonObject["bestsellerRanking"].asString
//        val awardList = jsonObject["awardList"].asJsonArray
        val description = jsonObject["description"].asString
        val descriptionSourceMallName = jsonObject["descriptionSourceMallName"].asString
        val publisherReview = jsonObject["publisherReview"].asString
        val publisherReviewSourceMallName = jsonObject["publisherReviewSourceMallName"].asString
//        val searchTagList = jsonObject["searchTagList"].asJsonArray
        val detailSpecImage = jsonObject["detailSpecImage"].asString
        val detailSpecImageList = jsonObject["detailSpecImageList"].asJsonArray.map { it.asString }.toList()
        val detailSpecImageOwnMallName = jsonObject["detailSpecImageOwnMallName"].asString
        val contentsHtml = jsonObject["contentsHtml"].asString
        val contentsSourceMallName = jsonObject["contentsSourceMallName"].asString
        val isbn = jsonObject["isbn"].asString
        val isOversea = jsonObject["isOversea"].asBoolean
        val pages = jsonObject["pages"].asInt
        val weight = jsonObject["weight"].asString
        val size = jsonObject["size"].asString
        val _statistics = jsonObject["statistics"].asJsonObject
        val statistics = _statistics.keySet().map {
            val book = _statistics[it].asJsonObject
            val name = it
            val totalCount = book["totalCount"].asInt
            val lowPrice = book["lowPrice"].asInt
            val lendLowPrice = if(book["lendLowPrice"] != null) book["lendLowPrice"].asInt else null
            BookStatistics(name, totalCount, lowPrice, lendLowPrice)
        }.toList()
//        val attributeList = jsonObject["attributeList"].asJsonArray
//        val seriesList = jsonObject["seriesList"].asJsonArray
//        val setList = jsonObject["setList"].asJsonArray
//        val originalOrTranslatedList = jsonObject["originalOrTranslatedList"].asJsonArray
//        val otherPublicationList = jsonObject["otherPublicationList"].asJsonArray
//        val otherCoverList = jsonObject["otherCoverList"].asJsonArray
//        val audioclipList = jsonObject["audioclipList"].asJsonArray
        val authorIntroList = jsonObject["authorIntroList"].asJsonArray.map {
            val authorId = it.asJsonObject["authorId"].asString
            val name = it.asJsonObject["name"].asString
            val authorType = it.asJsonObject["authorType"].asString
            val intro = it.asJsonObject["intro"].asString
            AuthorIntro(authorId,name,authorType,intro)
        }.toList()
        val authorIntroOwnMallName = jsonObject["authorIntroOwnMallName"].asString
//        val bookFromOtherPublisherList = jsonObject["bookFromOtherPublisherList"].asJsonArray
        val authorOtherProductList = jsonObject["authorOtherProductList"].asJsonArray.map {
            val json = it.asJsonObject
            val id = json["id"].asString
            val isAdult1 = json["isAdult"].asBoolean
            val publishDay1 = json["publishDay"].asString
            val thumbnailUrl1  = json["thumbnailUrl"].asString
            val title1 = json["title"].asString
            val crUrl = json["crUrl"].asString
            AuthorOtherProduct(id, isAdult1, publishDay1, thumbnailUrl1, title1, crUrl)
        }.toList()


        return BookCatalog(
            jsonObject = jsonObject,
            nvMid = nvMid,
            categoryId = categoryId,
            categoryName = categoryName,
            fullCategoryId = fullCategoryId,
            fullCategoryName = fullCategoryName,
            title = title,
            subtitle = subtitle,
            publisher = publisher,
            publishDay = publishDay,
            isNew = isNew,
            isBestseller = isBestseller,
            isAdult = isAdult,
            thumbnailUrl = thumbnailUrl,
            authorList = authorList,
//            illustratorList = illustratorList,
//            translatorList = translatorList,
            bestsellerRanking = bestsellerRanking,
//            awardList = awardList,
            description = description,
            descriptionSourceMallName = descriptionSourceMallName,
            publisherReview = publisherReview,
            publisherReviewSourceMallName = publisherReviewSourceMallName,
//            searchTagList = searchTagList,
            detailSpecImage = detailSpecImage,
            detailSpecImageList = detailSpecImageList,
            detailSpecImageOwnMallName = detailSpecImageOwnMallName,
            contentsHtml = contentsHtml,
            contentsSourceMallName = contentsSourceMallName,
            isbn = isbn,
            isOversea = isOversea,
            pages = pages,
            weight = weight,
            size = size,
            statistics = statistics,
//            attributeList = attributeList,
//            seriesList = seriesList,
//            setList = setList,
//            originalOrTranslatedList = originalOrTranslatedList,
//            otherPublicationList = otherPublicationList,
//            otherCoverList = otherCoverList,
//            audioclipList = audioclipList,
            authorIntroList = authorIntroList,
            authorIntroOwnMallName = authorIntroOwnMallName,
//            bookFromOtherPublisherList = bookFromOtherPublisherList,
            authorOtherProductList = authorOtherProductList
        )
    }

    private fun getCatalogNumber(link: String): String {
        return link.split("/").last()
    }
}

fun JsonObject.printPrettierJson(jsonObjectName: String) {
    val gson = GsonBuilder().setPrettyPrinting().create()
    println("printPrettierJson(${jsonObjectName}) ---------")
    println(gson.toJson(this.asJsonObject))
    println("END : printPrettierJson() -------------------------------------------")
}

data class BookCatalog(
    val jsonObject: JsonObject,
    val nvMid: String,
    val categoryId: String,
    val categoryName: String,
    val fullCategoryId: String,
    val fullCategoryName: String,
    val title: String,
    val subtitle: String,
    val publisher: String,
    val publishDay: String,
    val isNew: Boolean,
    val isBestseller: Boolean,
    val isAdult: Boolean,
    val thumbnailUrl: String,
    val authorList: List<String>,
//    val illustratorList: List<*>,
//    val translatorList: List<*>,
    val bestsellerRanking: String,
//    val awardList: List<*>,
    val description: String,
    val descriptionSourceMallName: String,
    val publisherReview: String,
    val publisherReviewSourceMallName: String,
//    val searchTagList: List<*>,
    val detailSpecImage: String,
    val detailSpecImageList: List<String>,
    val detailSpecImageOwnMallName: String,
    val contentsHtml: String,
    val contentsSourceMallName: String,
    val isbn: String,
    val isOversea: Boolean,
    val pages: Int,
    val weight: String,
    val size: String,
    val statistics: List<BookStatistics>,
//    val attributeList: List<*>,
//    val seriesList: List<*>,
//    val setList: List<*>,
//    val originalOrTranslatedList: List<*>,
//    val otherPublicationList: List<*>,
//    val otherCoverList: List<*>,
//    val audioclipList: List<*>,
    val authorIntroList: List<AuthorIntro>,
    val authorIntroOwnMallName: String,
//    val bookFromOtherPublisherList: List<*>,
    val authorOtherProductList: List<AuthorOtherProduct>
) {

}


//override fun toString(): String {
  //  val sb = StringBuilder()
    //val gson = GsonBuilder().setPrettyPrinting().create()
    //sb.append("BookCatalog(${title}) ---------\n")
    //sb.append(gson.toJson(jsonObject) + "\n")
    //sb.append("END : printPrettierJson() -------------------------------------------")
    //return sb.toString()
//}
data class BookStatistics(
    val name: String,
    val totalCount: Int,
    val lowPrice: Int,
    val lendLowPrice: Int?
)

data class AuthorIntro(
    val authorId: String,
    val name: String,
    val authorType: String,
    val intro: String
)

data class AuthorOtherProduct(
    val id: String,
    val isAdult: Boolean,
    val publishDay: String,
    val thumbnailUrl: String,
    val title: String,
    val crUrl: String
)