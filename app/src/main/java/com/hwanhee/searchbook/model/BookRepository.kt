package com.hwanhee.searchbook.model

import com.hwanhee.searchbook.base.Paging
import com.hwanhee.searchbook.base.SearchKeyword
import com.hwanhee.searchbook.db.BooksDao
import com.hwanhee.searchbook.di.IoDispatcher
import com.hwanhee.searchbook.model.remote.BookApi
import com.hwanhee.searchbook.model.ui.BooksItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BookRepository @Inject constructor(
    private val api: BookApi,
    private val dao: BooksDao,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    private var memoryCache: BooksItem? = null
    suspend fun latestNewBooks() = flow {
        memoryCache?.let {
            emit(it)
        }

        val items = dao.getBookItemAndDetails().map { it.detailEntity.toBookItem() }
        emit(BooksItem(items.count(), 1, items.toMutableList()))

        memoryCache = api.getBooks().toBooksItem()
        memoryCache?.let {
            emit(it)

            dao.deleteAllBooksItems()

            it.items.forEach { bookItem ->
                dao.insert(bookItem.toBooksItemEntity())
                dao.upsert(bookItem.toBookDetailEntity())
            }
        }
    }
    .flowOn(Dispatchers.IO)

    suspend fun search(keyword: SearchKeyword, paging: Paging) = flow {
        // 제거 해야되는 경우면 after 키워드를 먼저가져온 후, 첫번 째 키워드의 결과에서 중복되는 id값을 제거하여 emit 한다
        if(keyword.isKeywordSearch() && keyword.needSubs()) {
            val mergedBooksItem = BooksItem()
            val afterItem = api.search(keyword.afterKeyword, paging.page).toBooksItem()
            val idList = afterItem.items.map { it.isbn13 }

            val baseItem = api.search(keyword.baseKeyword, paging.page).toBooksItem()
            for (item in baseItem.items) {
                if(!idList.contains(item.isbn13)) {
                   mergedBooksItem.items.add(item)
                }
            }

            mergedBooksItem.total = baseItem.total
            mergedBooksItem.page = paging.page
            emit(mergedBooksItem)

//            if (mergedBooksItem.total > 10 &&
//                baseItem.items.count() < 10 &&
//                paging.increaseIfNeedMore()
//                    ) {
//
//
//            }
        }
        else {
            var maxCount = 0
            keyword.getKeywords().forEach {
                val item = api.search(it, paging.page).toBooksItem()
                maxCount = maxOf(maxCount, item.total)
                item.total = maxCount
                emit(item)
            }
        }
    }
    .flowOn(Dispatchers.IO)

    suspend fun getBookByIsbn13(isbn13: String) = flow {
        val bookEntity = dao.getBookDetailByIsbn13(isbn13)
        bookEntity?.let {
            emit(it.toBookItemDetail())
        }

        val response = api.getBookByIsbn13(isbn13)
        emit(response.toBookItemDetail())
        dao.upsert(response.toBookDetailEntity())
    }
    .flowOn(ioDispatcher)
}