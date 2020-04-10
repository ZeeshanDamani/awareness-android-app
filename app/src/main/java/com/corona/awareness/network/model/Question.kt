package com.corona.awareness.network.model

data class Question(
    val questionId: Int,
    val question: String,
    val questionType: String,
    val answers: List<Answer>,
    val defaultAnswerId: Int?,
    val dependentOn: QuestionAnswerPair?
)