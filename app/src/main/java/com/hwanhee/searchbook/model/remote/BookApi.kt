package com.hwanhee.searchbook.model.remote

import retrofit2.http.GET
import retrofit2.http.Path
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BookApi @Inject constructor(private val service: Service) {
    suspend fun getBooksNew(): BooksResponse = service.getBooksNew()
    suspend fun getBookByIsbn13(isbn13: String): BookDetailResponse =
        service.getBookByIsbn13(isbn13)
    suspend fun search(word: String, index: Int): BooksResponse =
        service.getBookBySearch(word, index)

    interface Service {
        @GET("new")
        suspend fun getBooksNew(): BooksResponse

        @GET("books/{isbn13}")
        suspend fun getBookByIsbn13(@Path("isbn13") isbn13: String): BookDetailResponse

        @GET("search/{word}/{index}")
        suspend fun getBookBySearch(
            @Path("word") word: String,
            @Path("index") index: Int
        ): BooksResponse
    }

    companion object {
        private const val API_SCHEME = "https://"
        private const val API_HOST = "api.itbook.store"
        private const val API_VERSION = "/1.0"
        private const val ROOT = "/"
        const val API_URL = API_SCHEME + API_HOST + API_VERSION + ROOT
    }
}