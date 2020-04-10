package com.corona.awareness.viewholder

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.appcompat.widget.AppCompatTextView
import com.corona.awareness.R
import com.corona.awareness.network.model.QuestionAnswerPair
import com.corona.awareness.viewmodel.QuestionViewModel

class DropDownViewHolder(view: View) : ViewHolder(view) {
    private val question: AppCompatTextView = itemView.findViewById(R.id.question)
    private val dropDown: AppCompatSpinner = itemView.findViewById(R.id.question_drop_down)
    private lateinit var adapter: ArrayAdapter<String>

    override fun bindView(viewModel: QuestionViewModel) {
        question.text = viewModel.question
        val data = viewModel.answers.map {
            it.answer
        }.toTypedArray()
        adapter = ArrayAdapter(itemView.context, android.R.layout.simple_spinner_item, data)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dropDown.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                viewModel.onAnswerSelected(
                    QuestionAnswerPair(
                        viewModel.questionId,
                        viewModel.answers[position].answerId
                    )
                )
                viewModel.selectedAnswer = viewModel.answers[position].answerId
            }

        }
        dropDown.adapter = adapter

        viewModel.selectedAnswer?.let {
            val index = viewModel.answers.indexOfFirst { answer -> answer.answerId == it }
            dropDown.setSelection(index)
        }
        adapter.notifyDataSetChanged()
    }
}