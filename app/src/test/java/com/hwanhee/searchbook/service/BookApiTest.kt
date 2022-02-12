package com.hwanhee.searchbook.service

import com.hwanhee.searchbook.TestHelper
import org.junit.Assert
import org.junit.Test

class BookApiTest {
    @Test
    fun `생성이 되어야한다`() {
        val service = TestHelper.getBookApiService()
        Assert.assertNotNull(service)
    }
}
