package com.hwanhee.searchbook.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.hwanhee.searchbook.model.ui.BookItem
import com.hwanhee.searchbook.model.ui.BooksItem

@Entity(tableName = "book_items",
    indices = [
        Index(value = ["isbn13"], unique = true)
    ]
)
data class BooksItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name="isbn13") val isbn13: String,
    @ColumnInfo(name="title") val title: String,
    @ColumnInfo(name="subtitle") val subtitle: String,
    @ColumnInfo(name="thumbnail_url") val thumbnailUrl: String,
)