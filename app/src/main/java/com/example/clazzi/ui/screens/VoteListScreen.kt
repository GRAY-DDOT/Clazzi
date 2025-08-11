package com.example.clazzi.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.clazzi.R
import com.example.clazzi.model.Vote
import com.example.clazzi.ui.theme.ClazziTheme
import com.example.clazzi.util.formatDate
import com.example.clazzi.viewmodel.VoteListViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoteListScreen(
    navController: NavController,
    viewModel: VoteListViewModel,
    onVoteClicked: (String) -> Unit
) {
    val voteList by viewModel.voteList.collectAsState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.vote_list_title)) },
                actions = {
                    IconButton(
                        onClick = {

                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "마이페이지"
                        )
                    }
                }
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
//                VoteItem(vote, onVoteClicked)
                // 두 가지 결과 같음
                VoteItem(vote) { voteId: String ->
                    onVoteClicked(voteId) // 콜백으로 넘기기 때문에 it이 필요함

                }
                /*
                * VoteItem(vote) {
                *     onVoteClicked(it)
                * }
                * //it : vote.id
                * // 중괄호 블럭이 함수임
                * // 매개 변수가 voteID : String
                * // 매개 변수가 1개인 경우 it 으로 대체 가능
                * //
                * */
            }
        }
    }
}

@Composable
private fun VoteItem(
    vote: Vote,
    onVoteClicked: (String) -> Unit
) {
    val user: FirebaseUser? = FirebaseAuth.getInstance().currentUser
    val currentUserId = user?.uid ?: "0"

    var hasVoted: Boolean by remember { mutableStateOf(false) }



    Card(
        modifier = Modifier
            .fillMaxSize()
            .clickable {
                onVoteClicked(vote.id)
//                            navController.navigate("vote")
            },
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier.weight(weight = 1f)
            ) {
                Text(vote.title, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "생성일 ${formatDate(vote.createAt)}"
                )
                Text("항목 개수: ${vote.optionCount}")
            }
            Text(if(hasVoted) "투표 완료" else "투표 안함")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VoteListScreenPreview() {

    ClazziTheme {
        VoteListScreen(
            navController = NavController(LocalContext.current),
            viewModel = viewModel(),
            onVoteClicked = {}
        )
    }
}