package com.corona.awareness.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.corona.awareness.viewholder.ViewHolder
import com.corona.awareness.viewholder.ViewHolderProvider
import com.corona.awareness.viewmodel.QuestionViewModel

class QuestionsAdapter(private val modelList: List<QuestionViewModel>) :
    RecyclerView.Adapter<ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolderProvider.provideViewHolder(viewType, parent)
    }

    override fun getItemCount(): Int {
        return modelList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(modelList[position])
    }

    override fun getItemViewType(position: Int): Int {
        return modelList[position].type.code
    }

    fun getItem(position: Int): QuestionViewModel {
        return modelList[position]
    }
}