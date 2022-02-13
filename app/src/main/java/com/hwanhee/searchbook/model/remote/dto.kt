package com.hwanhee.searchbook.model.remote

import com.google.gson.annotations.SerializedName
import com.hwanhee.searchbook.model.entity.BookDetailEntity
import com.hwanhee.searchbook.model.ui.BookItem
import com.hwanhee.searchbook.model.ui.BookItemDetail
import com.hwanhee.searchbook.model.ui.BooksItem

data class BooksResponse(
    @SerializedName("total") val total: String?,
    @SerializedName("page") val page: String?,
    @SerializedName("books") val books: List<BookResponse>?
)

data class BookResponse(
    @SerializedName("title") val title: String?,
    @SerializedName("subtitle") val subtitle: String?,
    @SerializedName("isbn13") val isbn13: String?,
    @SerializedName("price") val price: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("url") val url: String?,
)

data class BookDetailResponse(
    @SerializedName("error") val error: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("subtitle") val subtitle: String?,
    @SerializedName("authors") val authors: String?,
    @SerializedName("publisher") val publisher: String?,
    @SerializedName("language") val language: String?,
    @SerializedName("isbn10") val isbn10: String?,
    @SerializedName("isbn13") val isbn13: String?,
    @SerializedName("pages") val pages: String?,
    @SerializedName("year") val year: String?,
    @SerializedName("rating") val rating: String?,
    @SerializedName("desc") val desc: String?,
    @SerializedName("price") val price: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("url") val url: String?,
)