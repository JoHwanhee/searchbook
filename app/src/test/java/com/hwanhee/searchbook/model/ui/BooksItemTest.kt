package com.hwanhee.searchbook.model.ui


import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class BooksItemTest {
    @Test
    fun plus() {
        val books1 = BooksItem(total = 0, page = 1, items= mutableListOf())

        val items = mutableListOf<BookItem>()
        items.add(BookItem("1", "1", "1", "1"))
        items.add(BookItem("2", "2", "2", "2"))
        items.add(BookItem("3", "3", "3", "3"))

        val books2 = BooksItem(total = 3, page = 1, items= items)
        val newBook = books1 + books2

        assertThat(newBook.items.count(), equalTo(3))
        assertThat(newBook.total, equalTo(3))
    }

    @Test
    fun minus() {
        val items1 = mutableListOf<BookItem>()
        items1.add(BookItem("1", "1", "1", "1"))
        items1.add(BookItem("3", "3", "3", "3"))
        val books1 = BooksItem(total = 0, page = 1, items= items1)

        val items2 = mutableListOf<BookItem>()
        items2.add(BookItem("1", "1", "1", "1"))
        items2.add(BookItem("2", "2", "2", "2"))
        items2.add(BookItem("3", "3", "3", "3"))
        val books2 = BooksItem(total = 3, page = 1, items= items2)

        val newBook = books2 - books1
        assertThat(newBook.items.count(), equalTo(1))
        assertThat(newBook.total, equalTo(3))
    }
}