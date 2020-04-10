package com.corona.awareness.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.corona.awareness.viewmodel.QuestionViewModel

abstract class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    abstract fun bindView(viewModel: QuestionViewModel)
}