package com.example.clazzi.viewmodel

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clazzi.model.Vote
import com.example.clazzi.model.VoteOption
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class VoteListViewModel : ViewModel() {
    val db = Firebase.firestore

    private val _voteList = MutableStateFlow<List<Vote>>(emptyList())
    val voteList: StateFlow<List<Vote>> = _voteList

    fun getVoteById(voteId: String): Vote? {
        return voteList.value.find { it.id == voteId }
    }

    fun addVote(vote: Vote, context: Context, imageUri: Uri) {
        viewModelScope.launch {

            try {
                val storageRef = FirebaseStorage.getInstance().reference
                val imageRef = storageRef.child("images/${UUID.randomUUID()}.jpg")

                // 이미지 업로드
                val inputStream = context.contentResolver.openInputStream(imageUri)
                val uploadTask = inputStream?.let { imageRef.putStream(it).await() }

                // 다운로드 url 가져오기
                val downloadUrl = imageRef.downloadUrl.await().toString()


                val voteMap = hashMapOf(
                    "id" to vote.id,
                    "title" to vote.title,
                    "imageUrl" to downloadUrl,
                    "createAt" to FieldValue.serverTimestamp(),
                    "options" to vote.options.map {
                        hashMapOf(
                            "id" to it.id,
                            "optionText" to it.optionText,
                        )
                    }
                )
                db.collection("votes")
                    .document(vote.id)
                    .set(voteMap)
                    .await()
                Log.d("Firebase", "Vote added successfully")
//                db.collection("votes")
//                    .add(vote)
            } catch (e: Exception) {
                Log.e("Firebase", "Error adding vote", e)
            }
        }


    }

    fun setVote(vote: Vote) {
        viewModelScope.launch {
            try {
                db.collection("votes")
                    .document(vote.id)
                    .set(vote)
                    .await()
                Log.d("Firebase", "Vote updating successfully")
            } catch (e: Exception) {
                Log.e("Firebase", "Error updating vote", e)
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
            .orderBy("createAt", Query.Direction.DESCENDING)
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