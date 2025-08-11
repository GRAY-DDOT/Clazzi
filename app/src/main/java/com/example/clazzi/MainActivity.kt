package com.example.clazzi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.clazzi.ui.screens.AuthScreen
import com.example.clazzi.ui.screens.CreateVoteScreen
import com.example.clazzi.ui.screens.MyPageScreen
import com.example.clazzi.ui.screens.VoteListScreen
import com.example.clazzi.ui.screens.VoteScreen
import com.example.clazzi.ui.theme.ClazziTheme
import com.example.clazzi.viewmodel.VoteListViewModel
import com.google.firebase.auth.FirebaseAuth

//@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        enableEdgeToEdge()
        setContent {
            ClazziTheme {
                val navController = rememberNavController()
                val voteListViewModel: VoteListViewModel = viewModel<VoteListViewModel>()
                val isLoggedIn = FirebaseAuth.getInstance().currentUser != null
                NavHost(
                    navController = navController,
                    startDestination = if (!isLoggedIn) "authScreen" else "voteList"
                ) {
                    composable(route = "authScreen") {
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
                            androidx.navigation.navDeepLink {
                                uriPattern = "https://clazzi.com/vote/{voteId}"
                            }

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



