package com.corona.awareness.activities

import android.os.Bundle
import android.util.Log
import com.corona.awareness.Awareness
import com.corona.awareness.R
import com.corona.awareness.adapters.ServayAdapter
import com.corona.awareness.databinding.ActivityDiagnosisHistoryBinding
import com.corona.awareness.model.servay.SurveyResponseModel
import com.corona.awareness.network.RetrofitConnection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiagnosisHistoryActivity : BaseActivity() ,ServayAdapter.ViewHolder.servayListener{

    private lateinit var servayViewAdapter: ServayAdapter
    private lateinit var bindingView: ActivityDiagnosisHistoryBinding
    private lateinit var servayListener: ServayAdapter.ViewHolder.servayListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_diagnosis_history)
        bindingView = setContentViewDataBinding(R.layout.activity_diagnosis_history)
        setTitle("Profile")
        servayListener = this

        setupServeys()
    }



    private fun setupServeys() {

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
                        //Log.e("qq Data", "" + response.body()?.userSurveys?.get(0)?.assessmentTime)
                         bindingView.previousServayRecyclerview.adapter = ServayAdapter(response.body()!!.userSurveys,servayListener)


                    } else {
                        Log.e("qq error", "" + response.errorBody())
                    }

                } else {
                    goToLginActivity()
                }
            }

        })

    }

    override fun onClick(servayResponse: SurveyResponseModel.UserSurvey) {

    }
}
