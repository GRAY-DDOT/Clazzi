package com.example.clazzi.viewmodel

import androidx.lifecycle.ViewModel
import com.example.clazzi.model.Vote
import com.example.clazzi.model.VoteOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class VoteListViewModel : ViewModel() {
    private val _voteList = MutableStateFlow<List<Vote>>(emptyList())
    val voteList: StateFlow<List<Vote>> = _voteList

    fun getVoteById(voteId: String): Vote? {
        return voteList.value.find { it.id == voteId }
    }

    fun addVote(vote: Vote) {
        _voteList.value = _voteList.value + vote
    }

    init {
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

    }
}