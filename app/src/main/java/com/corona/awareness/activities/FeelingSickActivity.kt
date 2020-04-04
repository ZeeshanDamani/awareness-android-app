package com.corona.awareness.activities

import android.os.Bundle
import android.util.Log
import com.corona.awareness.Awareness
import com.corona.awareness.R
import com.corona.awareness.adapters.QuestionAnswerAdapter
import com.corona.awareness.databinding.ActivityFeelingSickBinding
import com.corona.awareness.helper.kotlin.Constants
import com.corona.awareness.helper.kotlin.Utils
import com.corona.awareness.model.questions.get_questions.QuestionResponseModel
import com.corona.awareness.model.questions.post_answers.request.PostAnswerRequestModel
import com.corona.awareness.model.questions.post_answers.response.PostAnswerResponseModel
import com.corona.awareness.network.RetrofitConnection
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeelingSickActivity : BaseActivity(), QuestionAnswerAdapter.ViewHolder.answerListener {


    private var questionId: Int = 0
    private var answerId: Int = 0
    private var postAnswer: PostAnswerRequestModel? = null
    private var listOfQuestions: List<QuestionResponseModel.Qustion> = listOf()
    val listofAnswers: MutableList<PostAnswerRequestModel.Answer> = mutableListOf()
    private lateinit var answerAdapter: QuestionAnswerAdapter
    private lateinit var bindingView: ActivityFeelingSickBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_feeling_sick)
        bindingView = setContentViewDataBinding(R.layout.activity_feeling_sick)

        setUpUI()


    }

    fun setUpUI() {

        var index: Int = 0

        addQuestions()



        bindingView.btNext.setOnClickListener {
            index++
            // Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()
            if (index < listOfQuestions.size) {

                // bindingView.tQuestion.text = listOfQuestions.get(index).question
                //  bindingView.answerRecyclerview.adapter = QuestionAnswerAdapter(listOfQuestions.get(index).answers,this)
                setAdapter(index, listOfQuestions)

            } else {
                bindingView.btNext.text = "Submit"
                if (index == listOfQuestions.size + 1) {
                    postAnswers();
                }
            }

        }

    }

    private fun postAnswers() {

        val progressBar = Utils.openProgressDialog(this)
        postAnswer?.answers = listofAnswers

        for (i in 0..listofAnswers.size - 1) {
            Log.e("qq ", "" + listofAnswers.get(i))
        }

        for (i in 0..getQuestionsAnswer().answers.size - 1) {
            Log.e("qq ", "" + getQuestionsAnswer().answers.get(i))
        }

        val call = RetrofitConnection.getAPIClient(Awareness.loginData?.token!!)
            .sendQuestionAnswers("" + Awareness.loginData?.user?.id, getQuestionsAnswer())
        call.enqueue(object : Callback<PostAnswerResponseModel> {
            override fun onFailure(call: Call<PostAnswerResponseModel>, t: Throwable) {
                progressBar.dialog.dismiss()
            }

            override fun onResponse(
                call: Call<PostAnswerResponseModel>,
                response: Response<PostAnswerResponseModel>
            ) {
                if (response.code() == 200) {
                    if (response.isSuccessful) {
                        if (response.body()?.success!!) {
                            progressBar.dialog.dismiss()
                            goToDashboardActivity()
                            Log.e("qq asswer", "" + response.body()?.success!!)
                        }
                    } else {
                        Log.e("qq", "" + response.errorBody())
                    }
                } else {
                    Log.e("qq", "" + response.errorBody())
                    goToLginActivity()
                }
            }

        })
    }

    private fun getQuestionsAnswer(): PostAnswerRequestModel {
        return PostAnswerRequestModel(
            Constants.latitude,
            Constants.longitude,
            listofAnswers
        )
    }


    fun addQuestions() {

        val call = RetrofitConnection.getAPIClient(Awareness.loginData!!.token).getAllQuestions()
        call.enqueue(object : Callback<QuestionResponseModel> {
            override fun onFailure(call: Call<QuestionResponseModel>, t: Throwable) {

            }

            override fun onResponse(
                call: Call<QuestionResponseModel>,
                response: Response<QuestionResponseModel>
            ) {
                if (response.code() == 200) {
                    if (response.isSuccessful) {
                        val questionResponse = response.body()
                        Log.e("qq response", "" + questionResponse!!.qustions.get(0).question)
                        listOfQuestions = questionResponse!!.qustions


                        setAdapter(0, listOfQuestions)


                    } else {
                        Log.e("qq Failed", "" + response.errorBody())
                    }
                }
            }

        })

    }

    private fun setAdapter(index: Int, listOfQuestions: List<QuestionResponseModel.Qustion>) {
        questionId = listOfQuestions.get(index).questionId
        if (listOfQuestions.get(index).question != null) {
            bindingView.tQuestion.text = listOfQuestions.get(index).question
        } else {
            bindingView.tQuestion.text = "??"
        }

        answerAdapter = QuestionAnswerAdapter(listOfQuestions.get(index).answers, this)
        bindingView.answerRecyclerview.adapter = answerAdapter
    }


    override fun onclick(answerId: Int) {
        this.answerId = answerId

        if (listofAnswers.size > 0) { //check if user change the answer

            for (answer in listofAnswers) {
                if (answer.questionId == this.questionId && answer.answerId != answerId) {
                    Log.e("2-questionId,answerId ", "" + answerId + "," + this.questionId)
                    listofAnswers.add(PostAnswerRequestModel.Answer(answerId, this.questionId))
                }
            }

        } else {
            Log.e("1-questionId,answerId ", "" + answerId + "," + this.questionId)
            listofAnswers.add(PostAnswerRequestModel.Answer(answerId, this.questionId))
        }

    }
}







