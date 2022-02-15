package com.hwanhee.searchbook.model
import androidx.test.platform.app.InstrumentationRegistry
import com.hwanhee.searchbook.TestHelper
import com.hwanhee.searchbook.base.Paging
import com.hwanhee.searchbook.base.SearchKeyword
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*

class BookRepositoryTest {
    private val ioThreadSurrogate = newSingleThreadContext("IO thread")
    lateinit var repository: BookRepository

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        repository = BookRepository(
            TestHelper.getBookApi(),
            TestHelper.getBookDao(appContext),
            ioThreadSurrogate
        )

        Dispatchers.setMain(ioThreadSurrogate)
    }

    @Test
    fun `새로운_책_호출_테스트_20개_여야한다`() = runBlocking {
        repository.getNewBooks().collect {
            Assert.assertNotNull(it)
            Assert.assertEquals(it.items.count(), 20)
        }
    }

    @Test
    fun `리스트_호출_후_상세보기`() = runBlocking {
        repository.getNewBooks().collect {
            Assert.assertNotNull(it)
            Assert.assertEquals(it.items.count(), 20)

            repository.getBookByIsbn13(it.items[0].isbn13).collect { bookDetail ->
                Assert.assertEquals(it.items[0].isbn13, bookDetail.isbn13)
            }
        }
    }

    @Test
    fun `검색_결과는_페이지당_10개_여야한다`() = runBlocking {
        repository.searchBooks(SearchKeyword("java"), Paging(currentPage = 1)).collect {
            Assert.assertNotNull(it)
            Assert.assertEquals(it.items.count(), 10)
        }
    }

    @Test
    fun `없는_페이지_결과는_0개_여야한다`() = runBlocking {
        repository.searchBooks(SearchKeyword("java"), Paging(currentPage = 10000)).collect {
            Assert.assertNotNull(it)
            Assert.assertEquals(it.items.count(), 0)
        }
    }

    @Test
    fun `키워드_검색_더하기는_emit_2개_여야한다`() = runBlocking {
        val searched = repository.searchBooks(SearchKeyword("java|android"), Paging(currentPage = 1))
        Assert.assertEquals(searched.count(), 2)
    }

    @Test
    fun `키워드_검색_더하기후_제목에_키워드가_포함되어야한다`() = runBlocking {
        val searched = repository.searchBooks(SearchKeyword("java|android"), Paging(currentPage = 1))
        Assert.assertEquals(searched.count(), 2)
        val booksItems = searched.take(2).toList()
        booksItems[0].items.forEach { bookItem ->
            Assert.assertEquals(bookItem.title.lowercase(Locale.getDefault()).contains("java"), true)
        }

        booksItems[1].items.forEach { bookItem ->
            Assert.assertEquals(bookItem.title.lowercase(Locale.getDefault()).contains("android"), true)
        }
    }

    @Test
    fun `키워드_검색_빼기는_emit_1개_여야한다`() = runBlocking {
        val searched = repository.searchBooks(SearchKeyword("java-android"), Paging(currentPage = 1))
        Assert.assertEquals(searched.count(), 1)
    }

    @Test
    fun `키워드_검색_빼기후_제목에_키워드가_미포함되어야한다`() = runBlocking {
        val searched = repository.searchBooks(SearchKeyword("java-android"), Paging(currentPage = 1))
        Assert.assertEquals(searched.count(), 1)
        val booksItem = searched.first()
        booksItem.items.forEach { bookItem ->
            Assert.assertEquals(bookItem.title.lowercase(Locale.getDefault()).contains("android"), false)
        }
    }

    @Test
    fun `같은_키워드_빼기후_개수가0개_여야한다`() = runBlocking {
        val searched = repository.searchBooks(SearchKeyword("java-java"), Paging(currentPage = 1))
        Assert.assertEquals(searched.count(), 1)
        val booksItem = searched.first()
        Assert.assertEquals(booksItem.items.count(), 0)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        ioThreadSurrogate.close()
    }
}
