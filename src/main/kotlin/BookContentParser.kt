import com.google.gson.*
import com.google.gson.stream.JsonReader
import org.jsoup.Connection
import org.jsoup.Jsoup

object BookContentParser {

    enum class DataType {
        XML, JSON
    }

    val NAVER_URI = StringBuilder("https://openapi.naver.com/v1/search/book.")


    fun getUriFromXML(): StringBuilder {
        return NAVER_URI.append("xml")
    }

    fun getUriFromJSON(): StringBuilder {
        return NAVER_URI.append("json")
    }

    fun searchBook(clientId: String, clientIdSecret: String, dataType: DataType, title: String, display: Int? = null, start: Int? = null, sort: String? = null): JsonArray {
        val uri = when(dataType) {
            DataType.XML -> getUriFromXML()
            DataType.JSON -> getUriFromJSON()
        }

        val utfForm = String(title.toByteArray(), charset("UTF-8"))

        uri.append("?query=$utfForm")
        display?.let { uri.append("&display=$it") }
        start?.let { uri.append("&start=$it")}
        sort?.let { uri.append("&sort=$it") }

        val response = Jsoup.connect(uri.toString())
            .header("X-Naver-Client-Id", clientId)
            .header("X-Naver-Client-Secret", clientIdSecret)
            .method(Connection.Method.GET)
            .ignoreContentType(true)
            .execute()
        val document = response.parse().select("body").text()
        println(document)

        val element = JsonParser().parse(document)
        return element.asJsonObject.get("items").asJsonArray
    }

    fun getContent(jsonArray: JsonArray, index: Int): String {
        val book = jsonArray.get(index).asJsonObject
        val link = book.get("link").toString().replace("\"", "")
        var html = Jsoup.connect(link)
            .method(Connection.Method.GET)
            .ignoreContentType(true)
            .execute().parse().select("div[class=infoItem_data_text__bUgVI]").get(2).html().toString()
        html = html.replace("<br>", "\n").replace("</br>", "\n")
        return html
    }

    fun getTitle(jsonArray: JsonArray, index: Int): String {
        val book = jsonArray.get(index).asJsonObject
        val title = book.get("title").toString().replace("\"", "")
        return title
    }
}
