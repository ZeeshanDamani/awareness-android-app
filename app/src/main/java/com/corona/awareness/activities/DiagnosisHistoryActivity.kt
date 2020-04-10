package com.corona.awareness.activities

import android.app.ProgressDialog
import android.os.Build
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.corona.awareness.Awareness
import com.corona.awareness.R
import com.corona.awareness.adapters.DiagnosesAdapter
import com.corona.awareness.databinding.ActivityDiagnosisHistoryBinding
import com.corona.awareness.network.RetrofitConnection
import com.corona.awareness.network.model.DiagnosticResult
import com.corona.awareness.network.model.SurveyResponseModel
import com.google.android.material.snackbar.Snackbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DiagnosisHistoryActivity : BaseActivity() {

    private var progressDialog: ProgressDialog? = null
    private lateinit var bindingView: ActivityDiagnosisHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView = setContentViewDataBinding(R.layout.activity_diagnosis_history)
        setUpToolBar()
        fetchSurveys()
    }

    private fun setUpToolBar() {
        setSupportActionBar(bindingView.toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setDisplayShowHomeEnabled(true)
            it.title = ""
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showProgressDialog() {
        progressDialog = ProgressDialog.show(
            this, "",
            "Please wait", true
        )
    }

    private fun resetProgressDialog() {
        progressDialog?.dismiss()
        progressDialog = null
    }

    override fun onDestroy() {
        super.onDestroy()
        resetProgressDialog()
    }

    private fun fetchSurveys() {
        showProgressDialog()
        RetrofitConnection.getAPIClient(Awareness.loginData?.token!!)
            .getUserSurveys(Awareness.loginData?.user?.id.toString())
            .enqueue(object : Callback<SurveyResponseModel> {
                override fun onFailure(call: Call<SurveyResponseModel>, t: Throwable) {
                    Snackbar.make(
                        bindingView.container,
                        "Please try again later",
                        Snackbar.LENGTH_SHORT
                    ).show()
                }

                override fun onResponse(
                    call: Call<SurveyResponseModel>,
                    response: Response<SurveyResponseModel>
                ) {
                    resetProgressDialog()
                    val data = response.body()!!
                    setUpDiagnoses(data.userDiagnoses)
                }
            })
    }

    private fun setUpDiagnoses(surveys: List<DiagnosticResult>) {
        val layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(
            this,
            layoutManager.orientation
        )
        val drawable = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            resources.getDrawable(R.drawable.item_divider, null)
        } else {
            resources.getDrawable(R.drawable.item_divider)
        }

        dividerItemDecoration.setDrawable(drawable)
        bindingView.diagnoses.addItemDecoration(dividerItemDecoration)
        bindingView.diagnoses.layoutManager = layoutManager
        bindingView.diagnoses.adapter = DiagnosesAdapter(surveys)
    }
}
