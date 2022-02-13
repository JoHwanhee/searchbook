package com.hwanhee.searchbook.db

import androidx.room.*
import com.hwanhee.searchbook.model.entity.BookDetailEntity
import com.hwanhee.searchbook.model.entity.BooksItemEntity

@Database(
    entities = [BooksItemEntity::class, BookDetailEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun booksDao(): BooksDao
}
 



