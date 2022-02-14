package com.hwanhee.searchbook.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.hwanhee.searchbook.model.ui.BookItemDetail

@Entity(tableName = "book_detail")
data class BookDetailEntity(
    @PrimaryKey(autoGenerate = false) val isbn13: String,
    @ColumnInfo(name = "title") val title: String? = null,
    @ColumnInfo(name = "subtitle") val subtitle: String? = null,
    @ColumnInfo(name = "authors") val authors: String? = null,
    @ColumnInfo(name = "publisher") val publisher: String? = null,
    @ColumnInfo(name = "language") val language: String? = null,
    @ColumnInfo(name = "isbn10") val isbn10: String? = null,
    @ColumnInfo(name = "pages") val pages: String? = null,
    @ColumnInfo(name = "year") val year: String? = null,
    @ColumnInfo(name = "rating") val rating: String? = null,
    @ColumnInfo(name = "desc") val desc: String? = null,
    @ColumnInfo(name = "price") val price: String? = null,
    @ColumnInfo(name = "image") val image: String? = null,
    @ColumnInfo(name = "url") val url: String? = null,
)