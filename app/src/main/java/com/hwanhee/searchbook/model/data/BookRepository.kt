package com.hwanhee.searchbook.model.data

import com.hwanhee.searchbook.model.BookItem
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(private val api: BookApi) {

    private var cachedCategories: List<BookItem>? = null
}