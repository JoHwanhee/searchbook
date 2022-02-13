package com.hwanhee.searchbook.base


import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class PagingTest {

    @Test
    fun getPage() {
        val page = Paging()
        assertThat(page.page, equalTo(1))
    }

    @Test
    fun increaseIfNeedMore() {
        val page = Paging()
        assertThat(page.page, equalTo(1))
        assertThat(page.increaseIfNeedMore(), equalTo(false))

        val page2 = Paging(currentPage = 1,total = 10, perCount = 5)
        assertThat(page2.increaseIfNeedMore(), equalTo(true))
        assertThat(page2.page, equalTo(2))

        val page3 = Paging(currentPage = 1, total = 10, perCount = 10)
        assertThat(page3.increaseIfNeedMore(), equalTo(false))
        assertThat(page3.page, equalTo(1))

        val page4 = Paging(currentPage = 1, total = 10, perCount = 5)
        assertThat(page4.increaseIfNeedMore(), equalTo(true))
        assertThat(page4.page, equalTo(2))
        assertThat(page4.increaseIfNeedMore(), equalTo(false))
        assertThat(page4.page, equalTo(2))
    }

    @Test
    fun plus() {
        // 더하고 나서 페이지가 안 올라가야 되는 경우
        val page = Paging()
        assertThat(page.page, equalTo(1))
        assertThat(page.increaseIfNeedMore(), equalTo(false))

        val page2 = Paging(currentPage = 1,total = 10, perCount = 2)
        assertThat(page2.increaseIfNeedMore(), equalTo(true))
        assertThat(page2.page, equalTo(2))

        page.plus(page2)
        assertThat(page.increaseIfNeedMore(), equalTo(false))
        assertThat(page.page, equalTo(1))

        // 더하고 나서 페이지가 올라가야 되는 경우
        val page3 = Paging(perCount = 2)
        assertThat(page3.page, equalTo(1))
        assertThat(page3.increaseIfNeedMore(), equalTo(false))

        val page4 = Paging(currentPage = 1,total = 10, perCount = 2)
        assertThat(page4.increaseIfNeedMore(), equalTo(true))
        assertThat(page4.page, equalTo(2))

        page3.plus(page4)
        assertThat(page3.increaseIfNeedMore(), equalTo(true))
        assertThat(page3.page, equalTo(2))
    }
}