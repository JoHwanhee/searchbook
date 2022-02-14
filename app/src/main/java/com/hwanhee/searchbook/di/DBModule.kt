package com.hwanhee.searchbook.di

import android.content.Context
import androidx.room.Room
import com.hwanhee.searchbook.db.BooksDao
import com.hwanhee.searchbook.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DBModule {
 
    @Singleton
    @Provides
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase = Room
            .databaseBuilder(context, AppDatabase::class.java, "search_app.db")
            .build()

//    @Singleton
//    @Provides
//    fun provideAppDatabaseTest(
//        @ApplicationContext context: Context
//    ): AppDatabase = Room
//        .databaseBuilder(context, AppDatabase::class.java, "test.db")
//        .build()


    @Singleton
    @Provides
    fun provideBooksDao(appDatabase: AppDatabase): BooksDao = appDatabase.booksDao()
}