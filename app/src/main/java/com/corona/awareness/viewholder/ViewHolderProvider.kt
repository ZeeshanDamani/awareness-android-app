package com.corona.awareness.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.corona.awareness.R
import com.corona.awareness.viewmodel.QuestionViewModel
import com.corona.awareness.viewmodel.ViewModelType

object ViewHolderProvider {
    fun provideViewHolder(viewType: Int, parent: ViewGroup): ViewHolder {
        val layoutRes = getLayout(viewType)
        val view = LayoutInflater.from(parent.context).inflate(layoutRes, parent, false)
        return getViewHolder(viewType, view)
    }

    private fun getLayout(viewType: Int): Int {
        return when (viewType) {
            ViewModelType.MCQ.code -> R.layout.question_mcq
            ViewModelType.DROP_DOWN.code -> R.layout.question_drop_down
            else -> throw Exception()
        }
    }

    private fun getViewHolder(viewType: Int, view: View): ViewHolder {
        return when (viewType) {
            ViewModelType.MCQ.code -> MCQViewHolder(view)
            ViewModelType.DROP_DOWN.code -> DropDownViewHolder(view)
            else -> object : ViewHolder(view) {
                override fun bindView(viewModel: QuestionViewModel) {
                }
            }
        }
    }
}