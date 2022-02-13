package com.hwanhee.searchbook.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hwanhee.searchbook.model.entity.BookDetailEntity
import com.hwanhee.searchbook.model.entity.BooksItemEntity

@Dao
interface BooksDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBook(item: BooksItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addBookItemDetail(toEntity: BookDetailEntity)

    @Query("SELECT * FROM book_items")
    suspend fun getAllBookItems(): List<BooksItemEntity>

    @Query("SELECT * FROM book_detail WHERE isbn13 = :isbn13")
    suspend fun getBookDetailByIsbn13(isbn13:String): BookDetailEntity?
}
 