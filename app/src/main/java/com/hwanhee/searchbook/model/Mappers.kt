package com.hwanhee.searchbook.model.mappers

import com.hwanhee.searchbook.base.Paging
import com.hwanhee.searchbook.model.entity.BookDetailEntity
import com.hwanhee.searchbook.model.entity.BooksItemEntity
import com.hwanhee.searchbook.model.remote.BookDetailResponse
import com.hwanhee.searchbook.model.remote.BooksResponse
import com.hwanhee.searchbook.model.ui.BookItem
import com.hwanhee.searchbook.model.ui.BookItemDetail
import com.hwanhee.searchbook.model.ui.BooksItem

fun BookItem.toEntity(): BooksItemEntity {
    return BooksItemEntity(
        isbn13 = this.isbn13,
        title = this.title,
        thumbnailUrl = this.thumbnailUrl,
        subtitle = this.description
    )
}

fun BookDetailResponse.toEntity() : BookDetailEntity {
    return BookDetailEntity(
        title= title?: "",
        subtitle= subtitle?: "",
        authors= authors?: "",
        publisher= publisher?: "",
        language= language?: "",
        isbn10= isbn10?: "",
        isbn13= isbn13?: "",
        pages= pages?: "",
        year= year?: "",
        rating= rating?: "",
        desc= desc?: "",
        price= price?: "",
        image= image?: "",
        url= url?: "",
    )
}

fun BooksItem.toPaging() : Paging {
    return Paging(
        currentPage = this.page,
        total = this.total,
        perCount = 10
    )
}

fun BooksResponse.toBooksItem(): BooksItem {
    if (this.books == null) return BooksItem(0, 0, mutableListOf())

    val books = this.books.map { book ->
        BookItem(
            isbn13 = book.isbn13 ?: "",
            title = book.title ?: "",
            description = book.subtitle ?: "",
            thumbnailUrl = book.image ?: ""
        )
    }

    val total: Int = if ( this.total == null) {
        books.count()
    }
    else {
        this.total.toInt()
    }

    val page: Int = if ( this.page == null) {
        1
    }
    else {
        this.page.toInt()
    }

    return BooksItem(
        total = total,
        page = page,
        books.toMutableList())
}

fun BookDetailResponse.toBookItemDetail(): BookItemDetail {
    return BookItemDetail(
        title= title?: "",
        subtitle= subtitle?: "",
        authors= authors?: "",
        publisher= publisher?: "",
        language= language?: "",
        isbn10= isbn10?: "",
        isbn13= isbn13?: "",
        pages= pages?: "",
        year= year?: "",
        rating= rating?: "",
        desc= desc?: "",
        price= price?: "",
        image= image?: "",
        url= url?: "",
    )
}


fun BookDetailEntity.toBookItemDetail() : BookItemDetail {
    return BookItemDetail(
        title= title?: "",
        subtitle= subtitle?: "",
        authors= authors?: "",
        publisher= publisher?: "",
        language= language?: "",
        isbn10= isbn10?: "",
        isbn13= isbn13?: "",
        pages= pages?: "",
        year= year?: "",
        rating= rating?: "",
        desc= desc?: "",
        price= price?: "",
        image= image?: "",
        url= url?: "",
    )
}

fun BooksItemEntity.toBookItem(): BookItem {
    return BookItem(
        isbn13 = this.isbn13,
        title = this.title,
        thumbnailUrl = this.thumbnailUrl,
        description = this.subtitle
    )
}