package com.corona.awareness.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.corona.awareness.R
import com.corona.awareness.model.questions.get_questions.QuestionResponseModel
import kotlinx.android.synthetic.main.answer_item_row.view.*

class QuestionAnswerAdapter(private val answerList: List<QuestionResponseModel.Qustion.Answer> = mutableListOf()
    ,private val answerListener: ViewHolder.answerListener)
    : RecyclerView.Adapter<QuestionAnswerAdapter.ViewHolder>() {


    private var selectedPosition: Int = -1

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
            if(answerList.get(position).answerText != null && answerList.get(position).answerText.isNotEmpty()){
                holder.cb_answer.text = answerList.get(position).answerText
                print("-- "+answerList.get(position).answerText);
            }else {
                holder.cb_answer.text = "???"
            }

            //row listener
            holder.cb_answer?.setOnClickListener{
                selectedPosition = position
                answerListener.onclick(answerList.get(position).answerId)
                notifyDataSetChanged()
            }

        if(selectedPosition == position){
            holder.cb_answer.isChecked = true

        }else{
            holder.cb_answer.isChecked = false
        }
    }



    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {

        val cb_answer = view.cb_answer


        interface answerListener{
            fun onclick(answerId: Int)
        }

    }






}