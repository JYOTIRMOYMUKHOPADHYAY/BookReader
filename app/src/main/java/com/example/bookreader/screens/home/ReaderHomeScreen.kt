package com.example.bookreader.screens.home


import android.util.Log
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun Home(navController: NavController = NavController(LocalContext.current)) {

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
            HomeContent(navController)
        }
    }
}


@Composable
fun HomeContent(navController: NavController) {
    val email = FirebaseAuth.getInstance().currentUser?.email
    val currentUsername =
        if (!email.isNullOrEmpty()) email?.split(
            "@"
        )?.get(0) else "N/A"

    var listOfBooks = listOf<MBook>(
        MBook("sdfds", "sadrfsdaf", "Asdsadasd", "ASdasd"),
        MBook("sdfds", "sadrfsdaf", "Asdsadasd", "ASdasd"),
        MBook("sdfds", "sadrfsdaf", "Asdsadasd", "ASdasd"),
        MBook("sdfds", "sadrfsdaf", "Asdsadasd", "ASdasd"),
        MBook("sdfds", "sadrfsdaf", "Asdsadasd", "ASdasd"),
        MBook("sdfds", "sadrfsdaf", "Asdsadasd", "ASdasd"),
        MBook("sdfds", "sadrfsdaf", "Asdsadasd", "ASdasd"),

    )
    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.Top) {
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
        ReadingRightNowArea(books = listOf(), navController = navController)
        TitleSection(label = "Reading List", modifier = Modifier.padding())
        BookListArea(listOfBooks = listOfBooks, navController = navController)

    }
}

@Composable
fun BookListArea(listOfBooks: List<MBook>, navController: NavController) {
    HorizontalScrollableComponent(listOfBooks){
        //TODO ONCLICK ON CARD NAVIGATE DETAILS
    }
}

@Composable
fun HorizontalScrollableComponent(listOfBooks: List<MBook>, onCardPress: (String) -> Unit) {
    val scrollState = rememberScrollState()
    Row(modifier = Modifier
        .fillMaxWidth()
        .heightIn(580.dp)
        .horizontalScroll(scrollState)) {
        for (book in listOfBooks) {
            ListCard(book){
                onCardPress(it)
                Log.d("OCC", "HorizontalScrollableComponent: ${it.toString()}")
            }
        }
    }
}

@Composable
fun ReadingRightNowArea(books: List<MBook>, navController: NavController) {
    ListCard()
}



