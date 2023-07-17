package com.example.bookreader.screens.home


import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.bookreader.Components.FABContent
import com.example.bookreader.Components.ListCard
import com.example.bookreader.Components.ReaderAppBar
import com.example.bookreader.Components.TitleSection
import com.example.bookreader.model.MBook
import com.example.bookreader.navigation.ReaderScreen
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    navController: NavController = NavController(LocalContext.current),
    viewModel: HomeScreenViewModel = hiltViewModel()
) {

    Scaffold(
        topBar = {
            ReaderAppBar(title = "A. Reader", navController = navController)
        },
        floatingActionButton = {
            FABContent {
                navController.navigate(ReaderScreen.SearchScreen.name)
            }
        }
    ) {
        Surface(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            HomeContent(navController, viewModel)
        }
    }
}


@Composable
fun HomeContent(navController: NavController, viewModel: HomeScreenViewModel) {
    val email = FirebaseAuth.getInstance().currentUser?.email
    val currentUsername =
        if (!email.isNullOrEmpty()) email?.split(
            "@"
        )?.get(0) else "N/A"

//    var listOfBooks = listOf<MBook>(
//        MBook("sdfds", "sadrfsdaf", "Asdsadasd", "ASdasd"),
//        MBook("sdfds", "sadrfsdaf", "Asdsadasd", "ASdasd"),
//        MBook("sdfds", "sadrfsdaf", "Asdsadasd", "ASdasd"),
//        MBook("sdfds", "sadrfsdaf", "Asdsadasd", "ASdasd"),
//        MBook("sdfds", "sadrfsdaf", "Asdsadasd", "ASdasd"),
//        MBook("sdfds", "sadrfsdaf", "Asdsadasd", "ASdasd"),
//        MBook("sdfds", "sadrfsdaf", "Asdsadasd", "ASdasd"),
//    )

    var listOfBooks = emptyList<MBook>()
    val currentUser = FirebaseAuth.getInstance().currentUser
    if (!viewModel.data.value.data.isNullOrEmpty()) {
        listOfBooks = viewModel.data.value.data!!.toList().filter { mBook ->
            mBook.userId == currentUser?.uid.toString()
        }
    }
    Column(modifier = Modifier.padding(20.dp).fillMaxSize()) {
        Row(
            modifier = Modifier
                .align(alignment = Alignment.Start)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TitleSection(label = "Your reading \n" + "activity right now.")
//            Spacer(modifier = Modifier.fillMaxWidth(0.7f))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(ReaderScreen.ReaderStatsScreen.name)
                        }
                        .size(45.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
                Text(
                    text = currentUsername.toString()
                        .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                    modifier = Modifier.padding(2.dp),
//                    textDecoration = TextDecoration.Underline,
                    color = Color.Red,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Clip
                )
            }

        }
        ReadingRightNowArea(listOfBooks = listOf(), navController = navController)
        TitleSection(label = "Reading List", modifier = Modifier.padding())
//        BookListArea(listOfBooks = listOfBooks, navController = navController)

    }
}

@Composable
fun BookListArea(listOfBooks: List<MBook>, navController: NavController) {

    val addedBooks = listOfBooks.filter { book ->
        book.startedReading == null && book.finishedReading == null
    }

    HorizontalScrollableComponent(addedBooks) {
        //TODO ONCLICK ON CARD NAVIGATE DETAILS
        Log.d("it", "BookListArea: ${it.toString()}")
        navController.navigate(ReaderScreen.UpdateScreen.name + "/$it")
    }
}

@Composable
fun HorizontalScrollableComponent(
    listOfBooks: List<MBook>,
    viewModel: HomeScreenViewModel = hiltViewModel(),
    onCardPress: (String) -> Unit
) {

    val scrollState = rememberScrollState()
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(780.dp)
            .horizontalScroll(scrollState)
    ) {
        if (viewModel.data.value.loading == true) {
            LinearProgressIndicator()
        }else{
            if(listOfBooks.isEmpty()) {
                Surface(modifier = Modifier.padding(23.dp)) {
                    Text(text = "No Book Found", style = TextStyle(color = Color.Red.copy(alpha = 0.4f), fontWeight = FontWeight.Bold, fontSize = 14.sp))
                }
            }else{
                for (book in listOfBooks) {
                    ListCard(book) {
                        onCardPress(book.googleBookId.toString())
                        Log.d("OCC", "HorizontalScrollableComponent: ${it.toString()}")
                    }
                }
            }
        }

    }
}

@Composable
fun ReadingRightNowArea(listOfBooks: List<MBook>, navController: NavController) {
    val readingNowList = listOfBooks.filter { book ->
        book.startedReading != null && book.finishedReading == null
    }


    HorizontalScrollableComponent(readingNowList) {
        //TODO ONCLICK ON CARD NAVIGATE DETAILS
        Log.d("it", "BookListArea: ${it.toString()}")
        navController.navigate(ReaderScreen.UpdateScreen.name + "/$it")
    }
}




