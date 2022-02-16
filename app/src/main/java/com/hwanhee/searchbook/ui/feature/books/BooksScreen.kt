package com.hwanhee.searchbook.ui.feature.books

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.hwanhee.searchbook.R
import com.hwanhee.searchbook.base.InfiniteListHandler
import com.hwanhee.searchbook.base.LAUNCH_LISTEN_FOR_EFFECTS
import com.hwanhee.searchbook.base.SearchKeyword
import com.hwanhee.searchbook.base.defaultCoilBuilder
import com.hwanhee.searchbook.base.logger.Logger.d
import com.hwanhee.searchbook.model.ui.BookItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch


@Composable
fun BooksScreen(
    state: BooksContract.State,
    searchTextState: String,
    effectFlow: Flow<BooksContract.Effect>?,
    onEventSent: (event: BooksContract.Event) -> Unit,
    onNavigationRequested: (navigationEffect: BooksContract.Effect.Navigation) -> Unit
) {
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val listState: LazyListState = rememberLazyListState()
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val networkError = stringResource(id = R.string.networkd_not_smooth)
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(LAUNCH_LISTEN_FOR_EFFECTS) {
        effectFlow?.onEach { effect ->
            when (effect) {
                is BooksContract.Effect.DataError ->
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = networkError,
                        duration = SnackbarDuration.Short
                    )
                is BooksContract.Effect.Navigation.ToBookDetails -> onNavigationRequested(
                    effect
                )
            }
        }?.collect()
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            MainAppBar(
                state = state,
                searchTextState = searchTextState,
                searchTextFocus = focusRequester,
                onTextChange = {
                    onEventSent(BooksContract.Event.UpdateSearchText(it))
                },
                onSearchClicked = {
                    onEventSent(BooksContract.Event.Search(SearchKeyword(it)))
                    focusManager.clearFocus()
                },
                onCloseClicked = {
                    coroutineScope.launch {
                        listState.animateScrollToItem(0)
                    }
                    onEventSent(BooksContract.Event.SearchOff)
                },
                onSearchTriggered = {
                    onEventSent(BooksContract.Event.SearchOn)
                }
            )
        },
    ) {
        Box {
            when {
                state.isLoading -> LoadingBar()
                state.books.isEmpty() -> {
                    NoItem()
                }
                else -> {
                    BooksList(
                        listState = listState,
                        bookItems = state.books,
                        isLoadingMoreState = state.isLoadingMore,
                        onItemClicked = { itemId -> onEventSent(BooksContract.Event.BookSelection(itemId))
                        },
                        onScrollInProgress = {
                            focusManager.clearFocus()
                        },
                        onScrollMeetsBottom = {
                            onEventSent(BooksContract.Event.ScrollMeetsBottom)
                        }
                    )

                }
            }
        }
    }
}

@Composable
fun NoItem() {
    Image(
        painterResource(R.drawable.ic_baseline_search_off_24),
        contentDescription = "",
        modifier = Modifier.fillMaxSize()
            .padding(150.dp)
    )
}

@Composable
fun MainAppBar(
    state: BooksContract.State,
    searchTextState: String,
    searchTextFocus: FocusRequester,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
    onSearchTriggered: () -> Unit
) {
    if(state.isSearchOpened) {
        SearchAppBar(
            text = searchTextState,
            searchTextFocus= searchTextFocus,
            onTextChange = onTextChange,
            onCloseClicked = onCloseClicked,
            onSearchClicked = onSearchClicked
        )
    }
    else {
        DefaultAppBar {
            onSearchTriggered.invoke()
        }
    }
}


@Composable
fun SearchAppBar(
    text: String,
    searchTextFocus: FocusRequester,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit,
) {
    TopAppBar(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        elevation = AppBarDefaults.TopAppBarElevation,
    ) {
        TextField(modifier = Modifier
            .fillMaxWidth()
            .focusRequester(searchTextFocus),
            value = text,
            onValueChange = {
                onTextChange(it)
            },
            placeholder = {
                Text(
                    modifier = Modifier
                        .alpha(ContentAlpha.medium),
                    text = stringResource(id = R.string.search),
                    color = Color.White
                )
            },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    modifier = Modifier
                        .alpha(ContentAlpha.medium),
                    onClick = {
                        onCloseClicked.invoke()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "close icon",
                        tint = Color.White
                    )
                }
            },
            trailingIcon =  {
                if(text.isEmpty()) null
                else
                IconButton(
                    onClick = {
                        onTextChange("")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "clear Icon",
                        tint = Color.White
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    onSearchClicked(text)
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                cursorColor = Color.White.copy(alpha = ContentAlpha.medium)
            ))
    }
    LaunchedEffect(Unit) {
        searchTextFocus.requestFocus()
    }
}

@Composable
fun DefaultAppBar(onSearchClicked: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.app_bar_title)
            )
        },
        actions = {
            IconButton(
                onClick = { onSearchClicked() }
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon",
                    tint = Color.White
                )
            }
        }
    )
}

@Composable
fun BooksList(
    listState: LazyListState,
    bookItems: List<BookItem>,
    isLoadingMoreState: Boolean,
    onItemClicked: (id: String) -> Unit = { },
    onScrollInProgress: () -> Unit = { },
    onScrollMeetsBottom: () -> Unit = { }
) {
    LazyColumn(
        state= listState,
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        items(bookItems) { item ->
            BookItemRow(item = item, onItemClicked = onItemClicked)
            Divider()
        }

        if (isLoadingMoreState) {
            item {
                LoadingBar()
            }
        }
    }

    if(listState.isScrollInProgress) {
        d("isScrollInProgress")
        onScrollInProgress.invoke()
    }

    InfiniteListHandler(listState = listState) {
        onScrollMeetsBottom.invoke()
    }
}

@Composable
fun BookItemRow(
    item: BookItem,
    onItemClicked: (id: String) -> Unit = { }
) {
    Row(modifier = Modifier
        .animateContentSize()
        .fillMaxWidth()
        .clickable { onItemClicked(item.isbn13) }
        .padding(16.dp)
    ) {
        Box(modifier = Modifier.align(alignment = Alignment.Top)) {
            BookItemThumbnail(item.url)
        }

        BookItemDetails(
            item = item,
            expandedLines = 2,
            modifier = Modifier
                .fillMaxWidth(0.80f)
                .align(Alignment.CenterVertically)
        )

        Box(
            modifier = Modifier.align(Alignment.Bottom)
        )
    }
}

@Composable
fun BookItemDetails(
    item: BookItem?,
    expandedLines: Int,
    modifier: Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = item?.title ?: "",
            textAlign = TextAlign.Start,
            style = MaterialTheme.typography.subtitle1,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        if (item?.subtitle?.trim()?.isNotEmpty() == true)
            CompositionLocalProvider(LocalContentAlpha provides ContentAlpha.medium) {
                Text(
                    text = item.subtitle.trim(),
                    textAlign = TextAlign.Start,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.caption,
                    maxLines = expandedLines
                )
            }
    }
}

@Composable
fun BookItemThumbnail(
    thumbnailUrl: String,
) {
    Image(
        painter = rememberImagePainter(
            data = thumbnailUrl,
            builder = defaultCoilBuilder()
        ),
        modifier = Modifier
            .size(120.dp),
        contentDescription = "Book thumbnail",
    )
}

@Composable
fun LoadingBar() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}