package com.hwanhee.searchbook.model.ui

data class BooksItem(
    var total: Int = 0,
    var page: Int = 1,
    val items: MutableList<BookItem> = mutableListOf()
)

operator fun BooksItem.plus(target: BooksItem) : BooksItem {
    val newItem = BooksItem()
    newItem.total = this.total + target.total
    newItem.items.addAll(target.items)
    return newItem
}

operator fun BooksItem.minus(target: BooksItem) : BooksItem {
    val newItem = BooksItem()

    val ids = target.items
        .map { it.isbn13 }

    this.items.forEach {
        if(!ids.contains(it.isbn13)) {
            newItem.items.add(it)
        }
    }

    newItem.total = this.total - target.total
    newItem.page = target.page

    return newItem
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
