package com.corona.awareness.viewholder

import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.widget.AppCompatTextView
import com.corona.awareness.R
import com.corona.awareness.network.model.QuestionAnswerPair
import com.corona.awareness.viewmodel.QuestionViewModel

class MCQViewHolder(view: View) : ViewHolder(view) {
    private val question: AppCompatTextView = itemView.findViewById(R.id.question)
    private val radioGroup: RadioGroup = itemView.findViewById(R.id.radio_group)

    override fun bindView(viewModel: QuestionViewModel) {
        question.text = viewModel.question
        radioGroup.removeAllViews()
        viewModel.answers.forEach { answer ->
            val radioButton = RadioButton(itemView.context)
            radioButton.id = View.generateViewId()
            radioButton.text = answer.answer

            if (answer.answerId == viewModel.selectedAnswer) {
                radioButton.isChecked = true
            }

            radioButton.setOnClickListener {
                viewModel.onAnswerSelected(
                    QuestionAnswerPair(
                        viewModel.questionId,
                        answer.answerId
                    )
                )
                viewModel.selectedAnswer = answer.answerId
            }
            radioGroup.addView(radioButton)
        }
    }
}