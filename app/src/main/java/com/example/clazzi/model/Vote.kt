package com.example.clazzi.model

data class Vote(
    val id: String,
    val title: String,
    val options: List<VoteOption>,
)

data class VoteOption (
    val id: String,
    val optionText: String,
)

//val voteList = listOf(
//    Vote(
//        id = "1",
//        title = "오늘 점심 뭐먹지?",
//        options = listOf(
//            VoteOption(id = "1", optionText = "짜장면"),
//            VoteOption(id = "2", optionText = "짬뽕"),
//            VoteOption(id = "3", optionText = "탕수육"),
//        )
//    ),
//    Vote(
//        id = "2",
//        title = "오늘 회식 어디?",
//        options = listOf(
//            VoteOption(id = "1", optionText = "비어킹"),
//            VoteOption(id = "2", optionText = "야시장"),
//            VoteOption(id = "3", optionText = "멕시칸"),
//        )
//    ),
//    Vote(
//        id = "3",
//        title = "오늘 점심 뭐먹지?",
//        options = listOf(
//            VoteOption(id = "1", optionText = "짜장면"),
//            VoteOption(id = "2", optionText = "짬뽕"),
//            VoteOption(id = "3", optionText = "탕수육"),
//        )
//    ),
//)