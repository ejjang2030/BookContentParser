package api

import com.google.gson.annotations.SerializedName

data class BookSearchResult(
        @SerializedName("lastBuildDate") val lastBuildDate: String,
        @SerializedName("total") val total: Int,
        @SerializedName("start") val start: Int,
        @SerializedName("display") val display: Int,
        @SerializedName("items") val items: List<BookResult>
) {
    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("[BookSearchResult]\n")
                .append("lastBuildDate : ${this.lastBuildDate}\n")
                .append("total : ${this.total}\n")
                .append("start : ${this.start}\n")
                .append("display : ${this.display}\n")
                .append("-items-\n")
                .append("$items")
        return sb.toString()
    }
}

data class BookResult(
        @SerializedName("title") val title: String,
        @SerializedName("link") val link: String,
        @SerializedName("image") val image: String,
        @SerializedName("author") val author: String,
        @SerializedName("discount") val discount: Int,
        @SerializedName("publisher") val publisher: String,
        @SerializedName("pubdate") val pubdate: String,
        @SerializedName("isbn") val isbn: String,
        @SerializedName("description") val description: String
) {
    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("title : ${this.title}\n")
                .append("link : ${this.link}\n")
                .append("image : ${this.image}\n")
                .append("author : ${this.author}\n")
                .append("discount : ${this.discount}\n")
                .append("publisher : ${this.publisher}\n")
                .append("pubdate : ${this.pubdate}\n")
                .append("isbn : ${this.isbn}\n")
                .append("description : \"${this.description}\"\n\n")
        return sb.toString()
    }
}