fun main(args: Array<String>) {
    BookContentParser.searchBook(SecretId.CLIENT_ID,
        SecretId.CLIENT_ID_SECRET,
        BookContentParser.DataType.JSON,
        "자바의 신",
        10)
}