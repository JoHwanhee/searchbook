package com.hwanhee.searchbook.db

import androidx.test.platform.app.InstrumentationRegistry
import com.hwanhee.searchbook.TestHelper
import com.hwanhee.searchbook.model.entity.BookDetailEntity
import com.hwanhee.searchbook.model.entity.BooksItemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class BooksDaoTest {
    private val ioThreadSurrogate = newSingleThreadContext("IO thread")
    lateinit var dao: BooksDao

    @Before
    fun setUp() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        dao = TestHelper.getBookDao(appContext)

        Dispatchers.setMain(ioThreadSurrogate)
    }

    @Test
    fun `기본_insert_와_get_이_잘되어야한다`() = runBlocking {
        val booksItemEntity = BooksItemEntity(
            isbn13 = "test1",
        )

        val booksItemEntity2 = BooksItemEntity(
            isbn13 = "test1",
        )

        val bookDetails = BookDetailEntity(
            isbn13 = "test1",
            title = "testTitle",
            subtitle = "testSubTitle",
        )

        dao.insert(booksItemEntity)
        dao.insert(booksItemEntity2)
        dao.upsert(bookDetails)

        val details = dao.getBookItemAndDetails()
        Assert.assertEquals(details[0].booksItem.isbn13, "test1")
        Assert.assertEquals(details[0].detailEntity.isbn13, "test1")
        Assert.assertEquals(details[0].detailEntity.title, "testTitle")
        Assert.assertEquals(details[0].detailEntity.subtitle, "testSubTitle")
    }

    @Test
    fun `BooksItemEntity은_중복_가능하다`() = runBlocking {
        val booksItemEntity = BooksItemEntity(
            isbn13 = "test1",
        )

        val booksItemEntity2 = BooksItemEntity(
            isbn13 = "test1",
        )

        val bookDetails = BookDetailEntity(
            isbn13 = "test1",
            title = "testTitle",
            subtitle = "testSubTitle",
        )

        dao.insert(booksItemEntity)
        dao.insert(booksItemEntity2)
        dao.upsert(bookDetails)

        val details = dao.getBookItemAndDetails()
        Assert.assertEquals(details[0].booksItem.isbn13, "test1")
        Assert.assertEquals(details[0].detailEntity.isbn13, "test1")
        Assert.assertEquals(details[0].detailEntity.title, "testTitle")
        Assert.assertEquals(details[0].detailEntity.subtitle, "testSubTitle")

        Assert.assertEquals(details[1].booksItem.isbn13, "test1")
        Assert.assertEquals(details[1].detailEntity.isbn13, "test1")
        Assert.assertEquals(details[1].detailEntity.title, "testTitle")
        Assert.assertEquals(details[1].detailEntity.subtitle, "testSubTitle")
    }

    @Test
    fun `bookDetail은_upsert가_기본_동작`() = runBlocking {
        val bookDetails = BookDetailEntity(
            isbn13 = "test1",
            title = "testTitle1",
            subtitle = "testSubTitle1",
        )

        dao.upsert(bookDetails)

        val details = dao.getBookItemAndDetails()
        Assert.assertEquals(details[0].booksItem.isbn13, "test1")
        Assert.assertEquals(details[0].detailEntity.isbn13, "test1")
        Assert.assertEquals(details[0].detailEntity.title, "testTitle1")
        Assert.assertEquals(details[0].detailEntity.subtitle, "testSubTitle1")


        val bookDetails2 = BookDetailEntity(
            isbn13 = "test1",
            title = "testTitle2",
            subtitle = "testSubTitle2",
        )

        dao.upsert(bookDetails2)

        val detail2 = dao.getBookItemAndDetails()
        Assert.assertEquals(detail2[0].booksItem.isbn13, "test1")
        Assert.assertEquals(detail2[0].detailEntity.isbn13, "test1")
        Assert.assertEquals(detail2[0].detailEntity.title, "testTitle2")
        Assert.assertEquals(detail2[0].detailEntity.subtitle, "testSubTitle2")
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        ioThreadSurrogate.close()
    }
}