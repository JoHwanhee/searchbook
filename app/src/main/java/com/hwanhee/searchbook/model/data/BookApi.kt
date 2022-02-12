package com.hwanhee.searchbook.model.data

import com.hwanhee.searchbook.model.response.BookResponse
import com.hwanhee.searchbook.model.response.BooksResponse
import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookApi @Inject constructor(private val service: Service) {

    suspend fun getBooks(): BooksResponse = service.getBooksNew()
    suspend fun getBookByIsbn13(isbn13: String): BookResponse =
        service.getBookByIsbn13(isbn13)

    interface Service {
        @GET("new")
        suspend fun getBooksNew(): BooksResponse

        @GET("books/{isbn13}")
        suspend fun getBookByIsbn13(@Path("isbn13") isbn13: String): BookResponse
    }

    companion object {
        private const val API_SCHEME = "https://"
        private const val API_HOST = "api.itbook.store"
        private const val API_VERSION = "/1.0"
        private const val ROOT = "/"
        const val API_URL = API_SCHEME + API_HOST + API_VERSION + ROOT
    }
}


