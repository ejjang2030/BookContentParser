import java.util.*

fun main(args: Array<String>) {
    val jsonArray = BookContentParser.searchBook(SecretId.CLIENT_ID,
        SecretId.CLIENT_ID_SECRET,
        BookContentParser.DataType.JSON,
        "알고리즘")
    println(jsonArray)
    for(i in 0..jsonArray.size() - 1) {
        println("$i 번 : ${jsonArray[i]}")
    }

    val scanner: Scanner = Scanner(System.`in`)
    print("번호를 입력하세요 : ")
    val number = scanner.nextInt()
    println("제목 : ${BookContentParser.getTitle(jsonArray, number)}")
    println("목차 ----------------------\n${BookContentParser.getContent(jsonArray, number)}")
}