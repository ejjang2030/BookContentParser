package com.ejjang2030.bookcontentparser

import com.google.gson.*
import com.google.gson.stream.JsonReader
import org.jsoup.Connection
import org.jsoup.Jsoup

object BookContentParser {
    fun getBookContentTableList(bookContentTable: String): MutableList<String> {
        return bookContentTable
            .replace("&amp;", "&")
            .replace("<b>", "")
            .replace("</b>", "")
            .split("<br>")
            .filter { it != "" }
            .toMutableList()
    }

    fun getContent(jsonArray: JsonArray, index: Int): String {
        val book = jsonArray.get(index).asJsonObject
        val link = book.get("link").toString().replace("\"", "")
        var html = Jsoup.connect(link)
            .method(Connection.Method.GET)
            .ignoreContentType(true)
            .execute().parse().select("div[class=infoItem_data_text__bUgVI]")
        var content: String = ""
        for(i in 0 until html.size) {
            println("$i 번째 : ${html.get(i).html()}")
            if(html.get(i).html().contains("<br>")) {
                content = html.get(i).html().toString()
            }
        }
        println()
        println()
        content = content.replace("&amp;", "&")
            .replace("<b>", "")
            .replace("</b>", "")

        return content
    }
}
