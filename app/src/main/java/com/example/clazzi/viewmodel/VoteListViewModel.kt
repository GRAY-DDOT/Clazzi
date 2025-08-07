package com.example.clazzi.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clazzi.model.Vote
import com.example.clazzi.model.VoteOption
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class VoteListViewModel : ViewModel() {
    val db = Firebase.firestore

    private val _voteList = MutableStateFlow<List<Vote>>(emptyList())
    val voteList: StateFlow<List<Vote>> = _voteList

    fun getVoteById(voteId: String): Vote? {
        return voteList.value.find { it.id == voteId }
    }

    fun addVote(vote: Vote) {
        viewModelScope.launch {

            try {
                db.collection("votes")
                    .document(vote.id)
                    .set(vote)
                    .await()
//                db.collection("votes")
//                    .add(vote)
            } catch (e: Exception) {
                Log.e("Firebase", "Error adding vote", e)
            }
        }


    }

    /*init {
        _voteList.value = listOf(
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

    }*/
    init {
        db.collection("votes")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    // 오류 처리
                    return@addSnapshotListener
                }
                if (snapshot != null) {
                    _voteList.value = snapshot.toObjects(Vote::class.java)
                }
            }
    }
}