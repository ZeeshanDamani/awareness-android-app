package com.corona.awareness.network.model

data class PostAnswerRequestModel(
    var latitude: Double,
    var longitude: Double,
    var answers: List<QuestionAnswerPair>
)