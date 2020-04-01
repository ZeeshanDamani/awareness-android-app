package com.corona.awareness.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.corona.awareness.R
import com.corona.awareness.model.servay.servayResponse
import kotlinx.android.synthetic.main.servay_item_row.view.*

class ServayAdapter(private val servayList: List<servayResponse.UserSurvey> = mutableListOf(),
                    private val servayListener: ViewHolder.servayListener):
        RecyclerView.Adapter<ServayAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.servay_item_row,parent,false))
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.time.setText("Time : "+servayList.get(position).assessmentTime)
        holder.t_desc_text.setText("lorem ipsum")
        holder.t_condition.setText("Condition")

        holder.itemView.setOnClickListener{
            if(servayListener != null){
                servayListener.onClick(servayList.get(position))
            }
        }

    }

    override fun getItemCount(): Int {
        Log.e("qq", "size" +servayList.size)
        if(servayList.size > 0){
            return servayList.size
        }
        else return 0
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

      //  val date = itemView.t_ass_date
        val time = itemView.t_assign_time
        val t_desc_text = itemView.t_desc_text
        val t_condition = itemView.t_condition

        interface servayListener{
            fun onClick(servayResponse: servayResponse.UserSurvey)
        }
    }

}