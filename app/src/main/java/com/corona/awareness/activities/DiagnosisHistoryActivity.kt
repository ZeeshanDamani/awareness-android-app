package com.corona.awareness.activities

import android.os.Bundle
import android.util.Log
import com.corona.awareness.Awareness
import com.corona.awareness.R
import com.corona.awareness.adapters.SurveyAdapter
import com.corona.awareness.databinding.ActivityDiagnosisHistoryBinding
import com.corona.awareness.network.RetrofitConnection
import com.corona.awareness.network.model.SurveyResponseModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiagnosisHistoryActivity : BaseActivity() {

    private lateinit var servayViewAdapter: SurveyAdapter
    private lateinit var bindingView: ActivityDiagnosisHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_diagnosis_history)
        bindingView = setContentViewDataBinding(R.layout.activity_diagnosis_history)
        setTitle("Profile")

        setupSurveys()
    }



    private fun setupSurveys() {

        val call =
            RetrofitConnection.getAPIClient(Awareness?.loginData?.token!!)
                .getUserSurveys(""+ Awareness.loginData?.user?.id)

        call.enqueue(object : Callback<SurveyResponseModel> {
            override fun onFailure(call: Call<SurveyResponseModel>, t: Throwable) {
                Log.e("qq P-error", "" + t.message)
            }

            override fun onResponse(
                call: Call<SurveyResponseModel>,
                response: Response<SurveyResponseModel>
            ) {
                if (response.code() == 200) {
                    if (response.isSuccessful) {
                         bindingView.previousServayRecyclerview.adapter = SurveyAdapter(response.body()!!.userSurveys)
                    } else {
                        Log.e("qq error", "" + response.errorBody())
                    }

                } else {
                    goToLginActivity()
                }
            }

        })

    }
}
