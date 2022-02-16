package com.hwanhee.searchbook

import android.content.Context
import androidx.room.Room
import com.hwanhee.searchbook.db.AppDatabase
import com.hwanhee.searchbook.db.BooksDao
import com.hwanhee.searchbook.di.BookApiProvider
import com.hwanhee.searchbook.di.DBModule
import com.hwanhee.searchbook.model.remote.BookApi

class TestHelper {
    companion object {
        fun getBookApi(): BookApi {
            val provider = BookApiProvider()
            val client = provider.provideAuthInterceptorOkHttpClient()
            val retrofit = provider.provideRetrofit(client)
            val service = provider.provideBookApiService(retrofit)
            return BookApi(service)
        }

        fun getBookDao(context: Context): BooksDao {
            val provider = DBModule()
            val appDatabase = Room.inMemoryDatabaseBuilder(
                context, AppDatabase::class.java).build()
            return provider.provideBooksDao(appDatabase)
        }
    }
}