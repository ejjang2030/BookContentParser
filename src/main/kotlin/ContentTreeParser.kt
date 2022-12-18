import java.util.*
import kotlin.Comparator

class ContentTreeParser(val list: List<String>) {
    val contentTokenList = mutableSetOf<String>()
    fun getTree(): Pair<MutableSet<String>, List<Pair<String, String>>> {
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
        return Pair(contentTokenList, listWithTokens)
    }

    override fun toString(): String {
        println("list length : ${list.size}")
        val (tokenSet, items) = getTree()
        println("목차 개요 : $tokenSet")
        val sb1 = StringBuilder()
        for(item in items.withIndex()) {
            for(token in tokenSet.withIndex()) {
                if(item.value.second == token.value) {
                    val sb = StringBuilder()
                    for(i in 0 until token.index) {
                        sb.append("  ")
                    }
                    sb.append(item.value.first)
                    sb.append("\n")
                    sb1.append(sb.toString())
                }
            }
        }
        return sb1.toString()
    }
}