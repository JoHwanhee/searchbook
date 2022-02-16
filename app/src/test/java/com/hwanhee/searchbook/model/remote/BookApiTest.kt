package com.hwanhee.searchbook.model.remote

import com.hwanhee.searchbook.TestHelper
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.HttpException

class BookApiTest {

    lateinit var api: BookApi

    @Before
    fun setUp() {
        api = TestHelper.getBookApi()
    }

    @Test
    fun getBooksNew() = runBlocking {
        flow {
            emit(api.getBooksNew())
        }
        .collect {
            Assert.assertNotNull(it.books)
            Assert.assertEquals(it.books?.size ?: 0, it.total?.toInt() ?: -1)
        }
    }

    @Test
    fun getBookByIsbn13() = runBlocking {
        flow {
            emit(api.getBookByIsbn13("1001643027241"))
        }
        .collect {
            Assert.assertEquals(it.title, "The Official Raspberry Pi Handbook 2022")
            Assert.assertEquals(it.subtitle, "")
            Assert.assertEquals(it.isbn13, "1001643027241")
            Assert.assertEquals(it.price, "\$0.00")
            Assert.assertEquals(it.image, "https://itbook.store/img/books/1001643027241.png")
            Assert.assertEquals(it.authors, "Wes Archer, David Crookes, PJ Evans, Gareth Halfacree, Rosie Hattersley, Phil King, Nicola King, KG Orphanides")
            Assert.assertEquals(it.publisher, "Raspberry Pi Press")
            Assert.assertEquals(it.language, "English")
            Assert.assertEquals(it.isbn10, "1643027247")
            Assert.assertEquals(it.pages, "204")
            Assert.assertEquals(it.year, "2021")
            Assert.assertEquals(it.rating, "0")
            Assert.assertEquals(it.desc, "Get even more from Raspberry Pi with the brand-new official Handbook! Over 200 pages of Raspberry Pi packed with inspirational projects, essential tutorials &amp; guides, practical tips and definitive reviews!Inside The Official Raspberry Pi Handbook 2022:- QuickStart guide to setting up your Raspbe...")
            Assert.assertEquals(it.image, "https://itbook.store/img/books/1001643027241.png")
        }
    }

    @Test
    fun `getBookByIsbn13_not_found`() = runBlocking {
        flow {
            emit(api.getBookByIsbn13("asedktjnwekrtjkj1k23"))
        }
        .catch {
            val exception = it as HttpException
            Assert.assertEquals(exception.code(), 404)
        }
        .collect {
            Assert.assertTrue(false)
        }
    }

    @Test
    fun `search_not_correct_page`() = runBlocking {
        flow {
            emit(api.search("hello", 0))
        }
        .collect {
            Assert.assertNotNull(it.books)
            Assert.assertEquals(it.page, "1")
        }
    }

    @Test
    fun `search_not_correct_page_2`() = runBlocking {
        flow {
            emit(api.search("hello", 100000000))
        }
        .collect {
            Assert.assertNotNull(it.books)
            Assert.assertEquals(it.books?.size ?: -1, 0)
            Assert.assertEquals(it.page, "1")
        }
    }

    @Test
    fun `search_correct_page`() = runBlocking {
        flow {
            emit(api.search("hello", 1))
        }
        .collect {
            Assert.assertNotNull(it.books)
            Assert.assertEquals(it.page, "1")
        }
    }

    @Test
    fun `search_not_found`() = runBlocking {
        flow {
            emit(api.search("asfklnasklf2jnk34jkncvnskldefnkj12k3123", 1))
        }
        .collect {
            Assert.assertEquals(it.page, "1")
            Assert.assertEquals(it.books?.size ?: -1, 0)
            Assert.assertEquals(it.total, "0")
        }
    }
}