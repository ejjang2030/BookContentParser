import api.naver.NaverSearching
import com.ejjang2030.bookcontentparser.BookContentParser
import java.util.*

class TestMain {
    fun main(args: Array<String>) {
        runBookSearch()
    }

    fun runBookSearch() {
        val naver = NaverSearching(SecretId.CLIENT_ID, SecretId.CLIENT_ID_SECRET)
        val scanner = Scanner(System.`in`)
        print("찾고자 하는 책 제목을 입력하세요 : ")
        var title = scanner.next()
        print("원하는 책 검색량을 입력하세요 : ")
        var count = scanner.nextInt()
        naver.searchBook(title, count) { call, res, t ->
            if(res != null) {
                if(res.isSuccessful) {
                    val bookResult = res.body()
                    if(bookResult != null) {
                        for(i in 0 until bookResult.items.size) {
                            println("$i 번 : ${bookResult.items[i].title}")
                        }
                        print("번호를 입력하세요 : ")
                        val number = scanner.nextInt()
                        naver.getBookCatalog(bookResult.items[number]) { call2, res2, bookCatalog, t2 ->
                            if(res2 != null) {
                                println("${bookCatalog?.bookResult}")
                                println("목차 ----------------------\n")
                                val list = BookContentParser.getBookContentTableList(bookCatalog?.descriptions?.contentTable ?: "")
                                val contentTree = ContentTreeParser(list)
                                println(contentTree)
                            } else {
                                println(t2!!.message)
                            }
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
}
