package com.example.clazzi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clazzi.model.Vote
import com.example.clazzi.repository.FirebaseVoteRepository
import com.example.clazzi.repository.VoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ViewModel 상속 잊지 말자!!
class VoteViewModel: ViewModel() {
    private val _vote =  MutableStateFlow<Vote?>(null)
    val vote: StateFlow<Vote?> = _vote

    val voteRepository: VoteRepository = FirebaseVoteRepository()

    fun loadVote(voteId: String) {
        viewModelScope.launch {
            voteRepository.observeVoteById(voteId).collect { vote ->
                _vote.value = vote
            }
        }
    }
}