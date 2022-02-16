package com.hwanhee.searchbook.base.logger

import com.hwanhee.searchbook.BuildConfig
import timber.log.Timber


class ConsoleLoggerAdapter : LoggerAdapter {
    init {
        Timber.plant(Timber.DebugTree())
    }

    override fun e(e: Throwable) {
        Timber.e(e)
    }

    override fun d(message: String) {
        if (BuildConfig.DEBUG) {

        }
        Timber.d(message)
    }
}