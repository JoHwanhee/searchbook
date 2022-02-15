package com.hwanhee.searchbook.base.logger

interface LoggerAdapter {
    fun e(e: Throwable)
    fun d(message: String)
}
