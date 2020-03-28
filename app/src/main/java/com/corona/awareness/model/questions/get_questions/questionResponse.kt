package com.corona.awareness.model.questions.get_questions

data class questionResponse(
    val qustions: List<Qustion>
) {
    data class Qustion(
        val answers: List<Answer>,
        val question: String?,
        val questionId: Int,
        val questionPoints: Int
    ) {
        data class Answer(
            val answerId: Int,
            val answerPoints: Int,
            val answerText: String
        )
    }
}