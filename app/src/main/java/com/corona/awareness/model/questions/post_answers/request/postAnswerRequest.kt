package com.corona.awareness.model.questions.post_answers.request

data class postAnswerRequest(
    var latitude: String,
    var longitude:String,
    var answers: MutableList<Answer>
) {
    data class Answer(
        val answerId: Int,
        val questionId: Int
    )
}