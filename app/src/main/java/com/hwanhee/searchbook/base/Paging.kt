package com.hwanhee.searchbook.base

class Paging(
    private var currentPage: Int = 1,
    private var total: Int = 0,
    private val perCount: Int = 10
) {
    val page get() = currentPage

    fun increaseIfNeedMore() : Boolean {
        val resNeedMore = needMore()
        if (resNeedMore) {
            currentPage += 1
        }

        return resNeedMore
    }

    private fun needMore() : Boolean{
        return total > currentPage * perCount
    }

    fun init() {
        currentPage = 1
        total = 10
    }

    fun plus(page: Paging) {
        this.total += page.total
    }
}