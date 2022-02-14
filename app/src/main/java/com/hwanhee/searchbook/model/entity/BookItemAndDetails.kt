package com.hwanhee.searchbook.model.entity

import androidx.room.Embedded
import androidx.room.Relation

data class BookItemAndDetails(
    @Embedded val booksItem: BooksItemEntity,
    @Relation(
         parentColumn = "isbn13",
         entityColumn = "isbn13"
    )
    val detailEntity: BookDetailEntity
)