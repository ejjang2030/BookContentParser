import api.BookSearchResult
import api.NaverSearching
import api.NaverSearchingService
import com.ejjang2030.bookcontentparser.BookContentParser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.swing.text.AbstractDocument.Content

fun main(args: Array<String>) {
//    test1()
    test2()
}

fun test1() {
    val scanner = Scanner(System.`in`)
    print("찾고자 하는 책 제목을 입력하세요 : ")
    var title = scanner.next()
    print("원하는 책 검색량을 입력하세요 : ")
    var count = scanner.nextInt()
    var jsonArray = BookContentParser.searchBook(SecretId.CLIENT_ID,
            SecretId.CLIENT_ID_SECRET,
            BookContentParser.DataType.JSON,
            title,
            display = count)
    println(jsonArray)
    for(i in 0 until jsonArray.size()) {
        println("$i 번 : ${jsonArray[i]}")
    }

    print("번호를 입력하세요 : ")
    var number = scanner.nextInt()
    scanner.reset()
    println("제목 : ${BookContentParser.getTitle(jsonArray, number)}")
    println("목차 ----------------------\n")
    var list = BookContentParser.getContentToList(jsonArray, number)
//    for(l in list) {
//        println(l)
//    }
//    println(list)

//    println("Tokenized : ${ContentTreeParser(list).getTree()}")

    var (tokenSet, items) = ContentTreeParser(list).getTree()

    for(item in items.withIndex()) {
        for(token in tokenSet.withIndex()) {
            if(item.value.second == token.value) {
                val sb = StringBuilder()
                for(i in 0 until token.index) {
                    sb.append("  ")
                }
                sb.append(item.value.first)
                println(sb)
            }
        }
    }
}

fun test2() {
    val retrofit = NaverSearching
            .getApiClient(SecretId.CLIENT_ID, SecretId.CLIENT_ID_SECRET)
            .create(NaverSearchingService::class.java)

    retrofit.searchBook("파이썬").enqueue(object: Callback<BookSearchResult> {
        override fun onResponse(call: Call<BookSearchResult>, response: Response<BookSearchResult>) {
            if(response.isSuccessful) {
                val result: BookSearchResult? = response.body()
                if(result != null) {
                    val items = result.items
                    items.forEachIndexed { index, item ->
                        println("$index : ${item.title}")
                    }
                    val scanner = Scanner(System.`in`)
                    print("번호를 입력하세요 : ")
                    val number = scanner.nextInt()
                    val bookInfo = items[number]

                } else {
                    println("검색결과가 없습니다.")
                }
            } else {
                println(response.message())
            }
        }

        override fun onFailure(call: Call<BookSearchResult>, t: Throwable) {
            println(t.message)
        }
    })
}