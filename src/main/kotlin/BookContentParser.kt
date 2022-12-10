import com.google.gson.JsonDeserializer
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonStreamParser
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

    fun searchBook(clientId: String, clientIdSecret: String, dataType: DataType, title: String, display: Int? = null, start: Int? = null, sort: String? = null) {
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
        var books = element.asJsonObject.get("items")
        println(books)
    }
}