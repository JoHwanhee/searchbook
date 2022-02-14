package com.hwanhee.searchbook.model.ui

data class BooksItem(
    var total: Int = 0,
    var page: Int = 1,
    val items: MutableList<BookItem> = mutableListOf()
) {
    fun plus(item: BooksItem) {
        this.total = this.total + item.total
        this.items.addAll(item.items)
    }
}

data class BookItem(
    val isbn13: String,
    val title: String,
    val subtitle: String = "",
    val url: String
)

data class BookItemDetail(
    val title: String,
    val subtitle: String,
    val authors: String,
    val publisher: String,
    val language: String,
    val isbn10: String,
    val isbn13: String,
    val pages: String,
    val year: String,
    val rating: String,
    val desc: String,
    val price: String,
    val image: String,
    val url: String,
)
