package com.example.clazzi.ui.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.clazzi.model.Vote
import com.example.clazzi.viewmodel.VoteListViewModel
import com.example.clazzi.viewmodel.VoteViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoteScreen(
    voteId: String,
    navController: NavController,
    voteListViewModel: VoteListViewModel
) {
    val voteViewModel: VoteViewModel = viewModel()

    LaunchedEffect(voteId) {
        voteViewModel.loadVote(voteId, voteListViewModel)
    }

    val vote: Vote? = voteViewModel.vote.collectAsState().value

    val user = FirebaseAuth.getInstance().currentUser
    val currentUserId = user?.uid ?: "0"

    var hasVoted: Boolean by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = vote) {
        if (vote != null) {
            hasVoted = vote.options.any { option ->
                option.voters.contains(currentUserId)
            }
        }
    }

    val totalVotes = vote?.options?.sumOf { it.voters.size } ?: 0

    var selectOption: Int by remember { mutableStateOf(0) }
    val coroutineScope: CoroutineScope = rememberCoroutineScope()


    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Magenta),
        topBar = {
            TopAppBar(
                title = { Text("투표") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigateUp() }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (vote != null) {
//                                val voteUrl = "https://clazzi-b011c.web.app/vote/${vote.id}"
                                val voteUrl = "https://clazzi-54344.web.app/vote/${vote.id}"
                                // Intent 앱/activity 이동간 전달할 정보를 답는 방법
                                val sendIntent =  android.content.Intent().apply {
                                    action = android.content.Intent.ACTION_SEND
                                    putExtra(android.content.Intent.EXTRA_TEXT, voteUrl)
                                    type = "text/plain"
                                }
                                navController.context.startActivity(Intent.createChooser(sendIntent, "투표 공유"))
                            }
                        }
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "투표 공유")
                    }
                }
            )
        }

    ) { innerPadding ->
        if (vote == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = buildAnnotatedString {
                        append("친구들과 ")
                        withStyle(
                            style =
                                SpanStyle(
                                    fontWeight = FontWeight.Bold
                                )
                        ) {
                            append("서로 투표")
                        }
                        append("하며\n")
                        withStyle(
                            style = SpanStyle(
                                fontWeight = FontWeight.Bold,
                            )
                        ) {
                            append("익명")
                        }
                        append("으로 마음을 전해요")
                    },
//                color = Color.White,
                    fontSize = 20.sp,
                )

                Spacer(Modifier.height(40.dp))
                Text(
                    text = vote.title,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
//                    color = Color.White
                    )
                )
                Spacer(Modifier.height((20.dp)))
                Image(
                    painter =
                        if (vote.imageUrl != null)
                            rememberAsyncImagePainter(vote.imageUrl)
                        else
                            painterResource(id = android.R.drawable.ic_menu_gallery),
                    contentDescription = "투표 사진",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )
                Spacer(Modifier.height(20.dp))

                if (!hasVoted) { // 투포를 안했을 때
                    vote.options.forEachIndexed { index, option ->
                        Button(
                            onClick = {
                                selectOption = index
                            },
                            colors = ButtonDefaults.buttonColors(
                                if (selectOption == index) Color(0xFF13F8A5)
                                else Color.LightGray.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier
                                .width(200.dp)
                                .padding(0.dp, 0.dp, 0.dp, 10.dp)
                        ) {
                            Text(
                                modifier = Modifier.padding(8.dp),
                                text = option.optionText,
                                color = Color.Black
//                        color = Color.White,
                            )
                        }
                    }
                } else {
                    vote.options.sortedByDescending { it.voters.size }
                        .forEach { option ->
                            val isMyVote = option.voters.contains(currentUserId)
                            val percent: Float = option.voters.size.toFloat() / totalVotes

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .background(
                                        if (isMyVote) Color(0xFF13F8A5).copy(alpha = 0.6f)
                                        else Color.LightGray.copy(alpha = 0.6f),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(12.dp),
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth(),
                                ) {
                                    Text(
                                        text = option.optionText,
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.weight(1f)
                                    )
                                    if (isMyVote) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "내가 투표한 항목",
                                            tint = Color.Black,
                                            modifier = Modifier.padding(start = 8.dp)
                                        )
                                    }
                                    Text(
                                        text = "${option.voters.size}",
                                        color = Color.Black,
                                        fontWeight = FontWeight.Bold
                                    )


                                }

                                Spacer(Modifier.width(4.dp))
                                LinearProgressIndicator(
                                    progress = { percent },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(8.dp)
                                        .clip(RoundedCornerShape(4.dp)),
                                    color = Color(0xFF13F8A5),
                                    trackColor = Color.LightGray.copy(alpha = 0.6f)

                                )
                            }
                        }

                }


                Spacer(Modifier.height(20.dp))
                Button(
                    onClick = {
                        if (!hasVoted) {
                            coroutineScope.launch {
                                val voterId = UUID.randomUUID().toString()

                                val selectedOption = vote.options[selectOption]

                                val updatedOption = selectedOption.copy(
                                    voters = selectedOption.voters + currentUserId
                                )

                                val updatedOptions = vote.options.mapIndexed { index, option ->
                                    if (index == selectOption) updatedOption else option
                                }

                                val updatedVote = vote.copy(
                                    options = updatedOptions
                                )

                                voteListViewModel.setVote(updatedVote)
                                hasVoted = true
                            }
                        }
                    },
                    enabled = !hasVoted,
                    modifier = Modifier.width(200.dp),
                ) {
                    Text(
                        if (hasVoted) "투표 완료"
                        else "투표 하기"
                    )
                }
            }
        }
    }
}

/*
@Composable
@Preview(showBackground = true)
fun VoteScreenPreview() {
    ClazziTheme {
        VoteScreen(
            navController = NavController(LocalContext.current),
            vote = Vote(
                id = "1",
                title = "오늘 점심 뭐먹지?",
                options = listOf(
                    VoteOption(id = "1", optionText = "짜장면"),
                    VoteOption(id = "2", optionText = "짬뽕"),
                    VoteOption(id = "3", optionText = "탕수육"),
                ),
            )
        )
    }
}*/
