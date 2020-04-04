package com.corona.awareness.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.corona.awareness.R
import com.corona.awareness.model.servay.SurveyResponseModel
import kotlinx.android.synthetic.main.servay_item_row.view.*

class SurveyAdapter(private val surveyList: List<SurveyResponseModel.UserSurvey>) :
    RecyclerView.Adapter<SurveyAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.servay_item_row,
                parent,
                false
            )
        )
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.time.text = "Time : $surveyList[position].assessmentTime"
        holder.t_desc_text.text = "lorem ipsum"
        holder.t_condition.text = "Condition"
    }

    override fun getItemCount(): Int {
        Log.e("qq", "size" + surveyList.size)
        return surveyList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val time = itemView.t_assign_time
        val t_desc_text = itemView.t_desc_text
        val t_condition = itemView.t_condition

    }

}