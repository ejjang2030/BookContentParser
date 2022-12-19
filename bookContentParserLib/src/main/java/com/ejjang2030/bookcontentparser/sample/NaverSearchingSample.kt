import api.naver.NaverSearching

class NaverSearchingSample {
    fun main() {
        val naver = NaverSearching(SecretId.NAVER_CLIENT_ID, SecretId.NAVER_CLIENT_ID_SECRET)
        naver.searchBook("자바스크립트") { call, response, throwable ->
            if (response != null) { // Retrofit의 onResponse()에 해당하는 블록 : onResponse에서 throwable을 null로 가져옴
                if(response.isSuccessful) {
                    val result = response.body() // BookSearchResult 객체로 가져옴
                    if(result != null) {
                        println(result)
                    }
                } else {
                    println(response.message())
                }
            } else { // Retrofit의 onFailure()에 해당하는 블록 : onFailure에서 response를 null로 가져옴
                println(throwable?.message)
            }
        }
    }
}