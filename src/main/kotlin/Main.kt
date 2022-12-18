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
    val naver = NaverSearching(SecretId.CLIENT_ID, SecretId.CLIENT_ID_SECRET)
    naver.searchBook("파이썬") { call, res, t ->
        if(res != null) {
            if(res.isSuccessful) {
                val bookResult = res.body()
                if(bookResult != null) naver.getBookCatalog(bookResult.items[0]) { call2, res2, bookCatalog, t2 ->
                    if(res2 != null) {
                        println("catalog : $bookCatalog")
                    } else {
                        println(t2!!.message)
                    }
                }
            }
        } else {
            if(t != null) {
                println(t.message)
            }
        }
    }
}

