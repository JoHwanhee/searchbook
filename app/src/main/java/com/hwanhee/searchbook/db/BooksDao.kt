package com.hwanhee.searchbook.db

import androidx.room.*
import com.hwanhee.searchbook.model.entity.BookDetailEntity
import com.hwanhee.searchbook.model.entity.BookItemAndDetails
import com.hwanhee.searchbook.model.entity.BooksItemEntity

@Dao
interface BooksDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: BooksItemEntity)

    @Query("delete from book_items")
    suspend fun deleteAllBooksItems()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: BookDetailEntity)

    @Query("SELECT * FROM book_items")
    suspend fun getAllBookItems(): List<BooksItemEntity>

    @Query("SELECT * FROM book_detail WHERE isbn13 = :isbn13")
    suspend fun getBookDetailByIsbn13(isbn13:String): BookDetailEntity?

    @Transaction
    @Query("SELECT * FROM book_items")
    fun getBookItemAndDetails(): List<BookItemAndDetails>
}
 