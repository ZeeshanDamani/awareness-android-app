package com.corona.awareness.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.corona.awareness.R
import com.corona.awareness.model.RecommendationMapper
import com.corona.awareness.model.StatusMapper
import com.corona.awareness.network.model.DiagnosticResult
import java.text.SimpleDateFormat
import java.util.*

class DiagnosesAdapter(private val surveyList: List<DiagnosticResult>) :
    RecyclerView.Adapter<DiagnosesAdapter.DiagnosisViewHolder>() {

    val formatter = SimpleDateFormat("dd MMMM - hh:m a", Locale.ENGLISH)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiagnosisViewHolder {
        return DiagnosisViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.layout_assessment_result,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DiagnosisViewHolder, position: Int) {
        holder.bind(surveyList[position])
    }

    override fun getItemCount(): Int {
        return surveyList.size
    }

    inner class DiagnosisViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val assessmentTime: AppCompatTextView = itemView.findViewById(R.id.assessmentTime)
        private val recommendation: AppCompatTextView = itemView.findViewById(R.id.recommendation)
        private val status: AppCompatTextView = itemView.findViewById(R.id.status)

        fun bind(diagnosis: DiagnosticResult) {
            assessmentTime.text = formatter.format(diagnosis.assessmentTime)
            recommendation.text = RecommendationMapper.map(diagnosis.recommendation)
            status.text = StatusMapper.map(diagnosis.status)
            status.setTextColor(Color.parseColor(StatusMapper.mapColor(diagnosis.status)))
        }
    }
}