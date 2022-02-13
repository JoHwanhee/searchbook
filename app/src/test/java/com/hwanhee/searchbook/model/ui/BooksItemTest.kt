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
        books1.plus(books2)

        assertThat(books1.items.count(), equalTo(3))
        assertThat(books1.total, equalTo(3))
    }
}