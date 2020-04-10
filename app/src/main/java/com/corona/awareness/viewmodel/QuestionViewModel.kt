package com.corona.awareness.viewmodel

import com.corona.awareness.network.model.QuestionAnswerPair

data class QuestionViewModel(
    val questionId: Int,
    val question: String,
    val answers: List<AnswerModel>,
    val type: ViewModelType,
    val questionAnswerPair: QuestionAnswerPair? = null,
    val onAnswerSelected: (model: QuestionAnswerPair) -> Unit = {},
    var selectedAnswer: Int? = null
)