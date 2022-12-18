package api.naver

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
            .append("description : \"${this.description}\"\n")
        return sb.toString()
    }
}


data class BookCatalogResult(
    val bookResult: BookResult,
    val catalogNumber: String,
    val mainTitle: String,
    val subTitle: String?,
    val category: String,
    val spec: BookSpec,
    val descriptions: BookDescriptions,
    val evaluations: BookEvaluations,
    val price: Int,
    val ebookPrice: Int?
) {
    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("[BookCatalogResult]\n")
        sb.append(bookResult)
        sb.append("catalogNumber=$catalogNumber\n")
        sb.append("mainTitle=$mainTitle\n")
        sb.append("subTitle=$subTitle\n")
        sb.append("category=$category\n")
        sb.append("spec=$spec\n")
        sb.append("descriptions----\n$descriptions\n")
        sb.append("evaluations=$evaluations\n")
        sb.append("price=$price\n")
        sb.append("ebookPrice=$ebookPrice\n")
        return sb.toString()
    }
}


data class BookSpec(
        val pages: Int,
        val weight: Int,
        val width: Int,
        val height: Int,
        val thickness: Int
)

data class BookDescriptions(
        val introduction: String,
        val publisherComment: String,
        val contentTable: String,
        val authorIntroduction: String
) {
    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("<introduction>\n")
        sb.append("${introduction.replace("<br>", "\n")}\n")
        sb.append("<publisherComment>\n")
        sb.append("${publisherComment.replace("<br>", "\n")}\n")
        sb.append("<contentTable>\n")
        sb.append("${contentTable.replace("<br>", "\n")}\n")
        sb.append("<authorIntroduction>\n")
        sb.append("${authorIntroduction.replace("<br>", "\n")}\n")
        return sb.toString()
    }
}

data class BookEvaluations(
        val starRate: Double? = null,
        val ranking: String? = null
)