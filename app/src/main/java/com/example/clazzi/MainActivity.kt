package com.example.clazzi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.clazzi.model.Vote
import com.example.clazzi.model.VoteOption
import com.example.clazzi.ui.screens.CreateVoteScreen
import com.example.clazzi.ui.screens.VoteListScreen
import com.example.clazzi.ui.screens.VoteScreen
import com.example.clazzi.ui.theme.ClazziTheme

//@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        enableEdgeToEdge()
        setContent {
            ClazziTheme {
                val navController = rememberNavController()
                val voteList = remember {
                    mutableStateListOf<Vote>(
                        Vote(
                            id = "1",
                            title = "오늘 점심 뭐먹지?",
                            options = listOf(
                                VoteOption(id = "1", optionText = "짜장면"),
                                VoteOption(id = "2", optionText = "짬뽕"),
                                VoteOption(id = "3", optionText = "탕수육"),
                            )
                        ),
                        Vote(
                            id = "2",
                            title = "오늘 회식 어디?",
                            options = listOf(
                                VoteOption(id = "1", optionText = "비어킹"),
                                VoteOption(id = "2", optionText = "야시장"),
                                VoteOption(id = "3", optionText = "멕시칸"),
                            )
                        ),
                        Vote(
                            id = "3",
                            title = "오늘 점심 뭐먹지?",
                            options = listOf(
                                VoteOption(id = "1", optionText = "짜장면"),
                                VoteOption(id = "2", optionText = "짬뽕"),
                                VoteOption(id = "3", optionText = "탕수육"),
                            )
                        ),
                    )
                }
                NavHost(
                    navController = navController,
                    startDestination = "voteList"
                ) {
                    composable(route = "voteList") {
                        VoteListScreen(
                            navController = navController,
                            voteList = voteList,
                            onVoteClicked = { voteId ->
                                navController.navigate("vote/$voteId")
                            }
                        )
                    }
                    composable(
                        route = "vote/{voteId}"
                    ) { backStackEntry ->
                        val voteId = backStackEntry.arguments?.getString("voteId") ?: ""
                        VoteScreen(
                            navController = navController,
                            vote = voteList.first { vote ->
                                vote.id == voteId
                            },
                        )
                    }

                    composable("createVote") {
                        CreateVoteScreen(
                            onVoteCreated = { vote ->
                                navController.popBackStack() // 뒤로 가기
                                voteList.add(vote)
                            }
                        )
                    }
                }
            }

        }
    }
}



