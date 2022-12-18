package api

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface NaverShoppingSearchingService {
    @GET("book/catalog/{catalogNumber}")
    fun getBookCatalog(
            @Path("catalogNumber") catalogNumber: String
    ): Call<ResponseBody>
}