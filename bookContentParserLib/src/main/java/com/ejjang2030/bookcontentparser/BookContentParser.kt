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
}
