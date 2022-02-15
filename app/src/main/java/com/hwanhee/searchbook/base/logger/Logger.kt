package com.hwanhee.searchbook.base.logger

import timber.log.Timber

object Logger {
    // todo : use di
    private val adapter: LoggerAdapter = ConsoleLoggerAdapter()

    fun e(e: Throwable) = adapter.e(e)
    fun d(message: String) = adapter.d(message)
}


