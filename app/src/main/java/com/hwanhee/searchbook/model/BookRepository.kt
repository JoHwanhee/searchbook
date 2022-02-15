package com.hwanhee.searchbook.model

import com.hwanhee.searchbook.base.Paging
import com.hwanhee.searchbook.base.SearchKeyword
import com.hwanhee.searchbook.db.BooksDao
import com.hwanhee.searchbook.di.IoDispatcher
import com.hwanhee.searchbook.model.remote.BookApi
import com.hwanhee.searchbook.model.ui.BooksItem
import com.hwanhee.searchbook.model.ui.minus
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    private val api: BookApi,
    private val dao: BooksDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getNewBooks() = flow {
        getCache()?.let {
            emit(it)
        }

        dao.getBookItemAndDetails()
            .map { it.detailEntity.toBookItem() }
            .toDefaultBooksItem()
            .let {
                emit(it)
            }

        api.getBooksNew()
            .toBooksItem()
            .let {
                emit(it)
                setCache(it)
                deleteAllDbItems()
                insertsAllDbItems(it)
            }
    }
    .flowOn(ioDispatcher)

    suspend fun getBookByIsbn13(isbn13: String) = flow {
        dao.getBookDetailByIsbn13(isbn13)
            ?.let {
                emit(it.toBookItemDetail())
            }

        api.getBookByIsbn13(isbn13)
            .let {
                dao.upsert(it.toBookDetailEntity())
                emit(it.toBookItemDetail())
            }
    }
    .flowOn(ioDispatcher)

    suspend fun searchBooks(keyword: SearchKeyword, paging: Paging) = flow {
        if(keyword.isKeywordSearch() && keyword.needSubs()) {
            // 제거 해야되는 경우면 after 키워드를 먼저가져온 후, 첫번 째 키워드의 결과에서 중복되는 id값을 제거하여 emit 한다
            val afterItem = api.search(keyword.afterKeyword, paging.page)
                .toBooksItem()

            api.search(keyword.baseKeyword, paging.page)
                .toBooksItem()
                .let { baseItem ->
                    emit(baseItem - afterItem)
                }
        }
        else {
            var maxCount = 0

            keyword.getKeywords()
                .forEach {
                    api.search(it, paging.page)
                        .toBooksItem()
                        .let { booksItem ->
                            maxCount = maxOf(maxCount, booksItem.total)
                            booksItem.total = maxCount
                            emit(booksItem)
                        }
            }
        }
    }
    .flowOn(ioDispatcher)

    /************************
     * Helpers in this class
     * *********************/
    private var memoryCache: BooksItem? = null

    private suspend fun deleteAllDbItems() {
        dao.deleteAllBooksItems()
    }

    private suspend fun insertsAllDbItems(item: BooksItem) {
        item.items.forEach { bookItem ->
            dao.insert(bookItem.toBooksItemEntity())
            dao.insert(bookItem.toBookDetailEntity())
        }
    }

    private fun getCache(): BooksItem? {
        return memoryCache
    }

    private fun setCache(item: BooksItem?) {
        memoryCache = item
    }
}