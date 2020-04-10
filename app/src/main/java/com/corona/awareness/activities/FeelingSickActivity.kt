package com.corona.awareness.activities

import android.app.ProgressDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.corona.awareness.Awareness
import com.corona.awareness.R
import com.corona.awareness.adapters.QuestionsAdapter
import com.corona.awareness.databinding.ActivityFeelingSickBinding
import com.corona.awareness.model.RecommendationMapper
import com.corona.awareness.model.StatusMapper
import com.corona.awareness.network.RetrofitConnection
import com.corona.awareness.network.model.PostAnswerRequestModel
import com.corona.awareness.network.model.PostAnswerResponseModel
import com.corona.awareness.network.model.QuestionAnswerPair
import com.corona.awareness.network.model.QuestionResponseModel
import com.corona.awareness.viewmodel.AnswerModel
import com.corona.awareness.viewmodel.QuestionViewModel
import com.corona.awareness.viewmodel.ViewModelType
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_feeling_sick.*
import kotlinx.android.synthetic.main.layout_assessment_result.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class FeelingSickActivity : BaseActivity() {

    private var progressDialog: ProgressDialog? = null
    private lateinit var bindingView: ActivityFeelingSickBinding
    private lateinit var questionsAdapter: QuestionsAdapter
    private var currentQuestionNumber = 0
    private var maxQuestion = 0
    private var surveyAnswers = mutableListOf<QuestionAnswerPair>()
    private var shouldSubmit: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingView = setContentViewDataBinding(R.layout.activity_feeling_sick)
        setUpToolBar()
        setupUI()
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

    private fun setupUI() {
        bindingView.nextQuestionBtn.setOnClickListener {
            if (shouldSubmit) {
                submitSurveyAnswers()
            } else {
                val nextQuestionNumber = getNextQuestionNumber()
                changeQuestion(nextQuestionNumber)
            }
        }

        bindingView.previousQuestionBtn.setOnClickListener {
            val previousQuestionNumber = getPreviousQuestionNumber()
            changeQuestion(previousQuestionNumber)
        }

        fetchQuestions()
    }

    private fun submitSurveyAnswers() {
        val index = surveyAnswers.indexOfFirst {
            it.answerId == null
        }

        if (index == -1) {
            showProgressDialog()
            postSurveyAnswers()
        } else {
            currentQuestionNumber = index
            changeQuestion(currentQuestionNumber)
        }
    }

    private fun getNextQuestionNumber(): Int {
        if (currentQuestionNumber < maxQuestion - 1) {
            for (i in currentQuestionNumber + 1 until maxQuestion) {
                val nextQuestion = questionsAdapter.getItem(i)
                if (nextQuestion.questionAnswerPair != null) {
                    val shouldShowQuestion = showShouldQuestion(nextQuestion.questionAnswerPair)
                    if (shouldShowQuestion) {
                        currentQuestionNumber = i
                        break
                    }
                } else {
                    currentQuestionNumber = i
                    break
                }
            }
        }
        return currentQuestionNumber
    }

    private fun getPreviousQuestionNumber(): Int {
        if (currentQuestionNumber > 0) {
            for (i in (currentQuestionNumber - 1) downTo 0) {
                val nextQuestion = questionsAdapter.getItem(i)
                if (nextQuestion.questionAnswerPair != null) {
                    val shouldShowQuestion = showShouldQuestion(nextQuestion.questionAnswerPair)
                    if (shouldShowQuestion) {
                        currentQuestionNumber = i
                        break
                    }
                } else {
                    currentQuestionNumber = i
                    break
                }
            }
        }

        return currentQuestionNumber
    }

    private fun showShouldQuestion(questionAnswerPair: QuestionAnswerPair): Boolean {
        return surveyAnswers.find {
            it.questionId == questionAnswerPair.questionId
        }?.answerId == questionAnswerPair.answerId
    }

    private fun changeQuestion(questionNumber: Int) {
        updateQuestionNumber(questionNumber + 1)
        questionsRecyclerView.scrollToPosition(questionNumber)

        var nextQuestionNumber = questionNumber
        for (i in questionNumber + 1 until maxQuestion) {
            val nextQuestion = questionsAdapter.getItem(i)
            if (nextQuestion.questionAnswerPair != null) {
                val shouldShowQuestion = showShouldQuestion(nextQuestion.questionAnswerPair)
                if (shouldShowQuestion) {
                    nextQuestionNumber = i
                    break
                }
            } else {
                nextQuestionNumber = i
                break
            }
        }

        if (nextQuestionNumber == questionNumber) {
            bindingView.nextQuestionBtn.text = "Submit"
            shouldSubmit = true
        } else {
            bindingView.nextQuestionBtn.text = "Next Question"
            shouldSubmit = false
        }
    }

    private fun updateQuestionNumber(currentQuestionNumber: Int) {
        bindingView.questionNumber.text = "QUESTION $currentQuestionNumber OF $maxQuestion"
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

    private fun fetchQuestions() {
        showProgressDialog()
        val call = RetrofitConnection.getAPIClient(Awareness.loginData!!.token).getAllQuestions()
        call.enqueue(object : Callback<QuestionResponseModel> {
            override fun onFailure(call: Call<QuestionResponseModel>, t: Throwable) {
                resetProgressDialog()
                bindingView.surveyForm.visibility = View.GONE
                Snackbar.make(
                    bindingView.container,
                    "Please try again later",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(
                call: Call<QuestionResponseModel>,
                response: Response<QuestionResponseModel>
            ) {
                val questionResponse = response.body()
                val viewModels = questionResponse!!.questions.map {
                    val viewModelType = if (it.questionType == QUESTION_TYPE_MCQ)
                        ViewModelType.MCQ
                    else ViewModelType.DROP_DOWN

                    val answerModels = it.answers.map { answer ->
                        AnswerModel(answer.answerId, answer.answerText)
                    }
                    surveyAnswers.add(QuestionAnswerPair(it.questionId, it.defaultAnswerId))
                    QuestionViewModel(
                        it.questionId,
                        it.question,
                        answerModels,
                        viewModelType,
                        it.dependentOn,
                        onAnswerSelected
                    )
                }

                setQuestions(viewModels)
                resetProgressDialog()
            }
        })
    }

    private fun postSurveyAnswers() {

        val call = RetrofitConnection.getAPIClient(Awareness.loginData!!.token)
            .sendQuestionAnswers(
                Awareness.loginData!!.user.id,
                PostAnswerRequestModel(
                    0.0,
                    0.0,
                    surveyAnswers
                )
            )
        call.enqueue(object : Callback<PostAnswerResponseModel> {
            override fun onFailure(call: Call<PostAnswerResponseModel>, t: Throwable) {
                resetProgressDialog()
                Snackbar.make(
                    bindingView.container,
                    "Please try again later",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

            override fun onResponse(
                call: Call<PostAnswerResponseModel>,
                response: Response<PostAnswerResponseModel>
            ) {
                resetProgressDialog()
                val postAnswerResponse = response.body()!!
                bindingView.surveyForm.visibility = View.GONE
                bindingView.resultGroup.visibility = View.VISIBLE
                bindingView.resultLayout.apply {
                    assessmentTime.text = SimpleDateFormat("dd MMMM - hh:m a", Locale.ENGLISH)
                            .format(postAnswerResponse.assessmentTime)
                    status.text = StatusMapper.map(postAnswerResponse.status)
                    status.setTextColor(Color.parseColor(StatusMapper.mapColor(postAnswerResponse.status)))
                    recomendation.text = RecommendationMapper.map(postAnswerResponse.recommendation)
                }
            }
        })
    }

    private fun setQuestions(viewModels: List<QuestionViewModel>) {
        maxQuestion = viewModels.size
        questionsAdapter = QuestionsAdapter(viewModels)
        val layoutManager =
            object : LinearLayoutManager(this, HORIZONTAL, false) {
                override fun canScrollHorizontally(): Boolean {
                    return false
                }

                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
        bindingView.questionsRecyclerView.layoutManager = layoutManager
        bindingView.questionsRecyclerView.adapter = questionsAdapter
        updateQuestionNumber(1)
    }

    private val onAnswerSelected: (pair: QuestionAnswerPair) -> Unit = { pair ->
        val index = surveyAnswers.indexOfFirst { it.questionId == pair.questionId }
        surveyAnswers[index] = pair
    }

    companion object {
        private const val QUESTION_TYPE_MCQ = "MCQ"
    }
}







