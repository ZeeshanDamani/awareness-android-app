package com.corona.awareness.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.corona.awareness.R
import com.corona.awareness.model.Answers
import kotlinx.android.synthetic.main.answer_item_row.view.*

class QuestionAnswerAdapter(private val answerList: MutableList<Answers> = mutableListOf()): RecyclerView.Adapter<QuestionAnswerAdapter.ViewHolder>() {


    fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflatedView = parent.inflate(R.layout.answer_item_row, false)
        return ViewHolder(inflatedView)
    }

    override fun getItemCount(): Int {
        if(answerList.size > 0) return answerList.size
        return 0;
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.cb_answer.text = answerList.get(position).text
            print("-- "+answerList.get(position).text);
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view), View.OnClickListener {

        val cb_answer = view.cb_answer

        override fun onClick(p0: View?) {

        }

    }

}