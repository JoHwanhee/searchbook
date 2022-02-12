package com.hwanhee.searchbook.model.response

import com.google.gson.annotations.SerializedName

data class BooksResponse(
    val total: String?,
    val page: String?,
    val books: List<BookResponse>?
)

data class BookResponse(
    @SerializedName("title") val title: String?,
    @SerializedName("subtitle") val subtitle: String?,
    @SerializedName("isbn13") val isbn13: String?,
    @SerializedName("price") val price: String?,
    @SerializedName("image") val image: String?,
    @SerializedName("url") val url: String?,
)