import java.util.*
import kotlin.Comparator

class ContentTreeParser(val list: List<String>) {
    val contentTokenList = mutableSetOf<String>()
    fun getTree(): TreeSet<Pair<String, String>> {
        val listWithTokens = list.map { element ->
            var token: String = ""
            for(i in 0 until RegexPattern.contentTokens.size) {
                val tokens = RegexPattern.contentTokens.toList()[i]
                if(element.contains(tokens.first)) {
                    contentTokenList.add(tokens.second)
                    token = tokens.second
                    break
                }
            }
            Pair(element, token)
        }.toList()

        println("목차 개요 : $contentTokenList")
        val comparator = Comparator { a: Pair<String, String>, b: Pair<String, String> ->
            return@Comparator contentTokenList.indexOf(a.second) - contentTokenList.indexOf(b.second)
        }
        val treeSet = TreeSet(comparator)
        listWithTokens.forEach { pair ->
            treeSet.add(pair)
        }

        return treeSet
    }
}