package com.example.clazzi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.example.clazzi.repository.RestApiRepository
import com.example.clazzi.repository.network.ApiClient
import com.example.clazzi.ui.screens.AuthScreen
import com.example.clazzi.ui.screens.CreateVoteScreen
import com.example.clazzi.ui.screens.MyPageScreen
import com.example.clazzi.ui.screens.VoteListScreen
import com.example.clazzi.ui.screens.VoteScreen
import com.example.clazzi.ui.theme.ClazziTheme
import com.example.clazzi.viewmodel.VoteListViewModel
import com.example.clazzi.viewmodel.VoteListViewModelFactory
import com.example.clazzi.viewmodel.VoteViewModel
import com.example.clazzi.viewmodel.VoteViewModelFactory
import com.google.firebase.auth.FirebaseAuth


//@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        enableEdgeToEdge()
        setContent {
            ClazziTheme {
                val navController = rememberNavController()

//                val repo = FirebaseVoteRepository()

                val repo = RestApiRepository(ApiClient.voteApiService)

                val voteListViewModel: VoteListViewModel = viewModel(
                    factory = VoteListViewModelFactory(repo)
                    )

                val voteViewModel: VoteViewModel = viewModel(
                    factory = VoteViewModelFactory(repo)
                )

                val isLoggedIn = FirebaseAuth.getInstance().currentUser != null
                NavHost(
                    navController = navController,
                    startDestination = if (!isLoggedIn) "auth" else "voteList"
                ) {
                    composable(route = "auth") {
                        AuthScreen(
                            navController = navController
                        )
                    }
                    composable(route = "myPage") {
                        MyPageScreen(
                            navController = navController
                        )
                    }

                    composable(route = "voteList") {
                        VoteListScreen(
                            navController = navController,
                            viewModel = voteListViewModel,
                            onVoteClicked = { voteId ->
                                navController.navigate("vote/$voteId")
                            }
                        )
                    }
                    composable(
                        route = "vote/{voteId}",
                        deepLinks = listOf(
                            navDeepLink { uriPattern = "clazzi://vote/{voteId}" },
//                            navDeepLink { uriPattern = "https://clazzi-b011c.web.app/vote/{voteId}" }
                            navDeepLink { uriPattern = "https://clazzi-54344.web.app/vote/{voteId}" }

                        )
                    ) { backStackEntry ->
                        val voteId = backStackEntry.arguments?.getString("voteId") ?: ""
                        VoteScreen(

                            voteId = voteId,
                            navController = navController,
                            voteListViewModel = voteListViewModel
                        )
                    }

                    composable("createVote") {
                        CreateVoteScreen(
                            viewModel = voteListViewModel,
                            navController = navController
                        )
                    }
                }
            }

        }
    }
}



