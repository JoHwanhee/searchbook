package com.hwanhee.searchbook

import com.hwanhee.searchbook.di.BookApiProvider
import com.hwanhee.searchbook.model.data.BookApi

class TestHelper {
    companion object {
        fun getBookApiService(): BookApi.Service {
            val provider = BookApiProvider()
            val client = provider.provideAuthInterceptorOkHttpClient()
            val retrofit = provider.provideRetrofit(client)
            return provider.provideBookApiService(retrofit)
        }
    }
}