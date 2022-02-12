package com.hwanhee.searchbook.model

data class BookItem(
    val id: String,
    val title: String,
    val thumbnailUrl: String,
    val description: String = ""
)
