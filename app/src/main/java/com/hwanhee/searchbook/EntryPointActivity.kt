package com.hwanhee.searchbook

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hwanhee.searchbook.ui.feature.books_details.BookDetailsScreen
import com.hwanhee.searchbook.ui.feature.books_details.BookDetailsViewModel
import com.hwanhee.searchbook.ui.feature.books.BooksContract
import com.hwanhee.searchbook.ui.feature.books.BooksScreen
import com.hwanhee.searchbook.ui.feature.books.BooksViewModel
import com.hwanhee.searchbook.NavigationKeys.Arg.BOOK_ISBN13
import com.hwanhee.searchbook.ui.theme.SearchBookTheme
import dagger.hilt.android.AndroidEntryPoint

// single activity
@AndroidEntryPoint
class EntryPointActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SearchBookTheme {
                SearchBookApp()
            }
        }
    }
}

@Composable
private fun SearchBookApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = NavigationKeys.Route.BOOKS_LIST) {
        composable(route = NavigationKeys.Route.BOOKS_LIST) {
            BooksDestination(navController)
        }
        composable(
            route = NavigationKeys.Route.BOOK_DETAILS,
            arguments = listOf(navArgument(BOOK_ISBN13) {
                type = NavType.StringType
            })
        ) {
            BookDetailsDestination()
        }
    }
}

@Composable
private fun BooksDestination(navController: NavHostController) {
    val viewModel: BooksViewModel = hiltViewModel()
    val state = viewModel.viewState.value
    BooksScreen(
        state = state,
        searchTextState = viewModel.searchTextState,
        effectFlow = viewModel.effect,
        onEventSent = { event -> viewModel.setEvent(event) },
        onNavigationRequested = { navigationEffect ->
            if (navigationEffect is BooksContract.Effect.Navigation.ToBookDetails) {
                navController.navigate("${NavigationKeys.Route.BOOKS_LIST}/${navigationEffect.isbn13}")
            }
        })
}

@Composable
private fun BookDetailsDestination() {
    val viewModel: BookDetailsViewModel = hiltViewModel()
    val state = viewModel.viewState.value
    val uriHandler = LocalUriHandler.current

    BookDetailsScreen(state) {
        if (it.isNotEmpty())
            uriHandler.openUri(it)
    }
}

object NavigationKeys {
    object Arg {
        const val BOOK_ISBN13 = "bookIsbn13"
    }

    object Route {
        const val BOOKS_LIST = "books_list"
        const val BOOK_DETAILS = "$BOOKS_LIST/{$BOOK_ISBN13}"
    }
}