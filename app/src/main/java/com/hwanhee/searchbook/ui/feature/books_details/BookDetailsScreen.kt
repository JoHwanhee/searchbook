package com.hwanhee.searchbook.ui.feature.books_details


import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.hwanhee.searchbook.R
import com.hwanhee.searchbook.base.LAUNCH_LISTEN_FOR_EFFECTS
import com.hwanhee.searchbook.base.LAUNCH_LISTEN_FOR_EFFECTS_DETAIL_VIEW
import com.hwanhee.searchbook.base.defaultCoilBuilder
import com.hwanhee.searchbook.model.ui.BookItemDetail
import com.hwanhee.searchbook.ui.feature.books.BooksContract
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach


@Composable
fun BookDetailsScreen(
    state: BookDetailsContract.State,
    effectFlow: Flow<BookDetailsContract.Effect>?,
    onUrlClick: (url: String) -> Unit,
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val errorMessage = stringResource(id = R.string.networkd_not_smooth)

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS_DETAIL_VIEW) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is BookDetailsContract.Effect.DataError ->
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = errorMessage,
                        duration = SnackbarDuration.Short
                    )
            }
        }?.collect()
    }

    val scrollState = rememberScrollState()

    Column(modifier = Modifier.verticalScroll(scrollState)){
        state.bookItemDetail?.let { bookItem ->
            Surface(elevation = 4.dp) {
                BookDetailsHeader(bookItem)
            }

            Spacer(modifier = Modifier.height(2.dp))

            BookDetails(bookItem, onUrlClick=onUrlClick)
        }
    }

}

@Composable
private fun BookDetails(
    bookItemDetail: BookItemDetail,
    onUrlClick: (url: String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        BookDetailsBody(
            item = bookItemDetail,
            onUrlClick= onUrlClick,
            modifier = Modifier
                .padding(16.dp)
        )
    }
}

@Composable
private fun HeaderInformation (
    item: BookItemDetail,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = item.title,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.h5,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = item.subtitle,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.subtitle1,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = item.authors,
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.subtitle2,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun BookDetailsHeader (
    item: BookItemDetail,
) {
    Row {
        Image(
            painter = rememberImagePainter(
                data = item.image,
                builder = defaultCoilBuilder(),
            ),
            modifier = Modifier.size(120.dp),
            contentDescription = "thumbnail picture",
        )

        HeaderInformation(
            item = item,
            modifier = Modifier
                .padding(
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 16.dp
                )
                .fillMaxWidth()
        )
    }
}

@Composable
private fun BookDetailsBody (
    item: BookItemDetail,
    modifier: Modifier,
    onUrlClick: (url: String) -> Unit,
) {
    Column(modifier = modifier) {
        BookDetailsBodyItem(stringResource(id = R.string.raing), item.rating, "0")

        Divider(Modifier.padding(top=16.dp, bottom = 8.dp))

        BookDetailsBodyItem(stringResource(id = R.string.language), item.language, "-")

        Divider(Modifier.padding(top=16.dp, bottom = 8.dp))

        BookDetailsBodyItem(stringResource(id = R.string.pages), item.pages, "0")

        Divider(Modifier.padding(top=16.dp, bottom = 8.dp))

        BookDetailsBodyItem(stringResource(id = R.string.year), item.year, "0")

        Divider(Modifier.padding(top=16.dp, bottom = 8.dp))

        BookDetailsBodyItem(stringResource(id = R.string.publisher), item.publisher, "-")

        Divider(Modifier.padding(top=16.dp, bottom = 8.dp))

        BookDetailsBodyItem(stringResource(id = R.string.price), item.price, "$0")

        Divider(Modifier.padding(top=16.dp, bottom = 8.dp))

        BookDetailsBodyClickableItem(stringResource(id = R.string.decscrition), item.desc, stringResource(id = R.string.read_more), item.url, onUrlClick)
    }
}

@Composable
private fun BookDetailsBodyItem (
    title: String,
    body: String,
    fallback: String
) {
    Text(text = title,  style = MaterialTheme.typography.h6)

    val bodyText = if (body.isEmpty()) fallback else body
    Text(text = bodyText,  style = MaterialTheme.typography.body1)
}


@Composable
private fun BookDetailsBodyClickableItem (
    title: String,
    body: String,
    tag: String,
    tagValue: String,
    onclick: (url: String) -> Unit,
) {
    Text(text = title,  style = MaterialTheme.typography.h6)
    val annotatedString = buildAnnotatedString {
        append(body)
        pushStringAnnotation(tag = "tag", annotation = tagValue)
        withStyle(style = SpanStyle(color = MaterialTheme.colors.primary)) {
            append(tag)
        }
        pop()
    }
    ClickableText(text = annotatedString, style = MaterialTheme.typography.body1, onClick = { offset ->
        annotatedString.getStringAnnotations(tag = "tag", start = offset, end = offset).firstOrNull()?.let {
            onclick.invoke(it.item)
        }
    })
}