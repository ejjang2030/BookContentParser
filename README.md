# BookContentParser
[![](https://jitpack.io/v/ejjang2030/BookContentParser.svg)](https://jitpack.io/#ejjang2030/BookContentParser)
[![License MIT](https://img.shields.io/badge/License-MIT-blue.svg?style=flat)](https://github.com/ejjang2030/BookContentParser/blob/main/LICENSE)
[![Public Yes](https://img.shields.io/badge/Public-yes-green.svg?style=flat)]()

네이버나 카카오 등의 API를 활용하여 책의 목차를 파싱하는 라이브러리

## Gradle 설정
root의 build.gradle 파일에 아래 코드와 같이 추가하여주세요.
```groovy
repositories {
    maven {
        url "https://jitpack.io"
    }
}
```

app의 build.gradle 파일에 아래 코드와 같이 추가하여주세요.

```groovy
dependencies {
    implementation 'com.github.ejjang2030:BookContentParser:1.0.5'
}
```

## 샘플 코드
### NaverSearching
```kotlin
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
```

### KakaoSearching
```kotlin
val kakao = KakaoSearching(SecretId.KAKAO_REST_API_KEY)
kakao.searchBook("자바스크립트") { call, response, throwable ->
    if (response != null) { // Retrofit의 onResponse()에 해당하는 블록 : onResponse에서 throwable을 null로 가져옴
        if(response.isSuccessful) {
            val result = response.body() // KakaoBookSearchResult 객체로 가져옴
            if(result != null) {
                println(result.documents)
            }
        } else {
            println(response.message())
        }
    } else { // Retrofit의 onFailure()에 해당하는 블록 : onFailure에서 response를 null로 가져옴
        println(throwable?.message)
    }
}
```

## 주요 사용 라이브러리
[![Retrofit 2.9.0](https://img.shields.io/badge/Retrofit-2.9.0-blue.svg?style=flat)]()
[![Jsoup 1.15.3](https://img.shields.io/badge/Jsoup-1.15.3-yellow.svg?style=flat)]()
[![Gson 2.9.0](https://img.shields.io/badge/Gson-2.9.0-green.svg?style=flat)]()

## 일부 시연 영상
[![일부 시연 영상](http://img.youtube.com/vi/Jx4xAcmqnrw/0.jpg)](https://youtu.be/Jx4xAcmqnrw)
