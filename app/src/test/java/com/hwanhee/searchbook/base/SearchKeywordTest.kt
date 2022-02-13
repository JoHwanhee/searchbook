package com.hwanhee.searchbook.base

import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat

import org.junit.Test

class SearchKeywordTest {

    @Test
    fun getBaseKeyword() {
        assertThat(SearchKeyword("hello-world").baseKeyword, equalTo("hello"))
        assertThat(SearchKeyword("-world").baseKeyword, equalTo(""))
        assertThat(SearchKeyword("-").baseKeyword, equalTo(""))
        assertThat(SearchKeyword("hello|world").baseKeyword, equalTo("hello"))
        assertThat(SearchKeyword("|world").baseKeyword, equalTo(""))
        assertThat(SearchKeyword("hello|").baseKeyword, equalTo("hello"))
        assertThat(SearchKeyword("|").baseKeyword, equalTo(""))
        assertThat(SearchKeyword("hello-|world").baseKeyword, equalTo(""))
        assertThat(SearchKeyword("helloworld").baseKeyword, equalTo(""))
    }

    @Test
    fun getAfterKeyword() {
        assertThat(SearchKeyword("hello-world").afterKeyword, equalTo("world"))
        assertThat(SearchKeyword("-world").afterKeyword, equalTo("world"))
        assertThat(SearchKeyword("hello-").afterKeyword, equalTo(""))
        assertThat(SearchKeyword("-").afterKeyword, equalTo(""))
        assertThat(SearchKeyword("hello|world").afterKeyword, equalTo("world"))
        assertThat(SearchKeyword("|world").afterKeyword,equalTo( "world"))
        assertThat(SearchKeyword("hello|").afterKeyword,equalTo( ""))
        assertThat(SearchKeyword("|").afterKeyword,equalTo( ""))
    }

    @Test
    fun getValues() {
        assertThat(SearchKeyword("hello-world").getKeywords()[0], equalTo("hello"))
        assertThat(SearchKeyword("hello-world").getKeywords()[1], equalTo("world"))
        assertThat(SearchKeyword("hello--world").getKeywords().count(), equalTo(1))
        assertThat(SearchKeyword("hello--world").getKeywords()[0], equalTo("hello--world"))
        assertThat(SearchKeyword("hello||world").getKeywords()[0], equalTo("hello||world"))
    }

    @Test
    fun isEmpty() {
        assertThat(SearchKeyword("").isEmpty(), equalTo(true))
    }

    @Test
    fun isKeywordSearch() {
        assertThat(SearchKeyword("").isKeywordSearch(), equalTo(false))
        assertThat(SearchKeyword("hello||world").isKeywordSearch(), equalTo(false))
        assertThat(SearchKeyword("hello-|world").isKeywordSearch(), equalTo(false))
        assertThat(SearchKeyword("hello|||||||-world").isKeywordSearch(), equalTo(false))
        assertThat(SearchKeyword("hello|-------world").isKeywordSearch(), equalTo(false))
        assertThat(SearchKeyword("hello|-------world").isKeywordSearch(), equalTo(false))
        assertThat(SearchKeyword("hello|world").isKeywordSearch(), equalTo(true))
        assertThat(SearchKeyword("hello-world").isKeywordSearch(), equalTo(true))
        assertThat(SearchKeyword("\"hello-world\"").isKeywordSearch(), equalTo(false))
    }

    @Test
    fun needSubs() {
        assertThat(SearchKeyword("hello-|world").needSubs(), equalTo(false))
        assertThat(SearchKeyword("hello|world").needSubs(), equalTo(false))
        assertThat(SearchKeyword("hello-world").needSubs(), equalTo(true))
    }
}