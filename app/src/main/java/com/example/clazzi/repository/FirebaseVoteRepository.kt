package com.example.clazzi.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.clazzi.model.Vote
import com.google.firebase.Firebase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID


class FirebaseVoteRepository : VoteRepository {
    val db = Firebase.firestore


    override fun observeVote(): Flow<List<Vote>> = callbackFlow {
        val listener: ListenerRegistration = db.collection("votes")
            .orderBy("createAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firebase", "Error observing votes", error)
                    close(error)
                } else if (snapshot != null) {
                    val votes = snapshot.toObjects(Vote::class.java)
                    trySend(votes)
                }

            }
        awaitClose { listener.remove() }
    }

    override suspend fun addVote(
        vote: Vote,
        context: Context,
        imageUri: Uri
    ) {
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
        } catch (e: Exception) {
            Log.e("Firebase", "Error adding vote", e)
        }
    }

    override suspend fun setVote(vote: Vote) {
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

    override fun observeVoteById(voteId: String): Flow<Vote?> = callbackFlow {
        /*
        * Firebase.firestore.collection("votes").document(voteId)
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

        * */


        val listener: ListenerRegistration = db.collection("votes")
            .document(voteId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("Firebase", "Error observing vote", error)
                    close(error)
                    return@addSnapshotListener
                } else if (snapshot != null) {
                    val vote = snapshot.toObject(Vote::class.java)
                    trySend(vote)
                }
            }
        awaitClose { listener.remove() }
    }
}