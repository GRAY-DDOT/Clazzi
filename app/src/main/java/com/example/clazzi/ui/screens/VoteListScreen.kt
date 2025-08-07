package com.example.clazzi.ui.screens

import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.clazzi.model.Vote
import com.example.clazzi.model.VoteOption
import com.example.clazzi.ui.theme.ClazziTheme
import kotlin.collections.listOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoteListScreen(
    navController: NavController,
    voteList: List<Vote>,
    onVoteClicked: (String) -> Unit
) {


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("투표 목록") }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate("createVote")
                },
            ) {
                Icon(Icons.Default.Add, contentDescription = "투표 만들기")
            }
        }

    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(voteList) { vote ->
                Card(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable {
                            onVoteClicked(vote.id)
//                            navController.navigate("vote")
                        },
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {
                        Text(vote.title)
                    }
                }

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VoteListScreenPreview() {

    ClazziTheme {
        VoteListScreen(
            navController = NavController(LocalContext.current),
            listOf(
                Vote(
                    id = "1", title = "오늘 점심 뭐먹지?", options = listOf(
                        VoteOption(id = "1", optionText = "짜장면"),
                        VoteOption(id = "2", optionText = "짬뽕"),
                        VoteOption(id = "3", optionText = "탕수육"),
                    )
                ),
                Vote(
                    id = "2", title = "오늘 회식 어디?", options = listOf(
                        VoteOption(id = "1", optionText = "비어킹"),
                        VoteOption(id = "2", optionText = "야시장"),
                        VoteOption(id = "3", optionText = "멕시칸"),
                    )
                ),
                Vote(
                    id = "3", title = "오늘 점심 뭐먹지?", options = listOf(
                        VoteOption(id = "1", optionText = "짜장면"),
                        VoteOption(id = "2", optionText = "짬뽕"),
                        VoteOption(id = "3", optionText = "탕수육"),
                    )
                ),
            ),
            onVoteClicked = {/*TODO*/}
        )
    }
}