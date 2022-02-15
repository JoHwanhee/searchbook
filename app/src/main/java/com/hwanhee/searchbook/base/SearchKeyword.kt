package com.hwanhee.searchbook.base

class SearchKeyword(_value: String) {
    private var _operator: Operator = Operator.NONE

    private var _baseKeyword = ""
    val baseKeyword get() = _baseKeyword

    private var _afterKeyword = ""
    val afterKeyword get() = _afterKeyword

    private var searchValue: String = _value.trim()

    init {
        initSearchMode(searchValue)
    }

    /**
     * 키워드 검색 데이터 세팅한다..
     * -+| OR |+- OR ".+" 인경우는 키워드 검색을 하지 않는다.
     * 예시 1) -가 여러번 인경우, --|
     * 예시 2) |이 여러번 인경우, ||-
     * 예시 3) 쌍따옴표로 묶은경우, "java-android"
     * */
    private fun initSearchMode(value: String) {
        val reg = """-+\||\|+-|".+"""".toRegex()
        val isNotKeywordSearch = reg.containsMatchIn(value)

        when {
            isNotKeywordSearch -> {
                _operator = Operator.NONE
            }
            value.contains("|") -> {
                val sliced = value.split("|")
                _operator = if (sliced.size == 2) Operator.PLUS else Operator.NONE
                _baseKeyword = sliced[0].trim()
                _afterKeyword = sliced[1].trim()
            }
            value.contains("-") -> {
                val sliced = value.split("-")
                _operator = if (sliced.size == 2) Operator.MINUS else Operator.NONE
                _baseKeyword = sliced[0].trim()
                _afterKeyword = sliced[1].trim()
            }
        }
    }

    fun getKeywords() : List<String> {
        return if (_operator == Operator.NONE) {
            listOf(searchValue)
        } else {
            listOf(baseKeyword, afterKeyword)
        }
    }

    fun isEmpty() : Boolean {
        return searchValue.isEmpty()
    }

    fun isKeywordSearch() : Boolean {
        return _operator != Operator.NONE
    }

    fun needSubs() : Boolean {
        return _operator == Operator.MINUS
    }

    private enum class Operator {
        NONE,
        PLUS,
        MINUS
    }
}