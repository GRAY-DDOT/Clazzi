package com.example.clazzi

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.clazzi.model.Vote
import com.example.clazzi.ui.screens.AuthScreen
import com.example.clazzi.ui.screens.CreateVoteScreen
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
                    startDestination = if(!isLoggedIn) "authScreen" else "voteList"
                ) {
                    composable(route = "authScreen") {
                        AuthScreen(
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
                        route = "vote/{voteId}"
                    ) { backStackEntry ->
                        val voteId = backStackEntry.arguments?.getString("voteId") ?: ""
                        val vote:Vote? = voteListViewModel.getVoteById(voteId)
                        if(vote != null) {
                            VoteScreen(
                                vote = vote,
                                navController = navController,
                                viewModel = voteListViewModel
                            )
                        }
                        else {
                            val context = LocalContext.current
                            Toast.makeText(context, "투표를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
                        }
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



