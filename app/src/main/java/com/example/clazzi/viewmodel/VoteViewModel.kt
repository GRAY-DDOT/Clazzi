package com.example.clazzi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clazzi.model.Vote
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ViewModel 상속 잊지 말자!!
class VoteViewModel: ViewModel() {
    private val _vote =  MutableStateFlow<Vote?>(null)
    val vote: StateFlow<Vote?> = _vote

    fun loadVote(voteId: String, voteListViewModel: VoteListViewModel) {
        Firebase.firestore.collection("votes").document(voteId)
            // addSnapshotListener => 문서가 변경될 때마다 호출
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (snapshot != null && snapshot.exists()) {
                    val vote = snapshot.toObject(Vote::class.java)
                    _vote.value = vote
                }
            }

    }
}