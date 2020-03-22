package com.corona.awareness

import android.os.Bundle
import android.widget.Toast
import com.corona.awareness.adapters.QuestionAnswerAdapter
import com.corona.awareness.databinding.ActivityFeelingSickBinding
import com.corona.awareness.model.Answers
import com.corona.awareness.model.Questions
import kotlin.random.Random

class FeelingSickActivity : BaseActivity() {

    private lateinit var bindingView : ActivityFeelingSickBinding
    val listOfQuestions: MutableList<Questions> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // setContentView(R.layout.activity_feeling_sick)
        bindingView = setContentViewDataBinding(R.layout.activity_feeling_sick)

        setUpUI()


    }

    fun setUpUI(){

        var index : Int = 0

        addQuestions()



        bindingView.btNext.setOnClickListener{

            index++
            Toast.makeText(this, "Clicked", Toast.LENGTH_SHORT).show()

            if(index < listOfQuestions.size) {

                bindingView.tQuestion.text = listOfQuestions.get(index).text
                // var answerAdapter = QuestionAnswerAdapter(listOfQuestions.get(index).answer)
                bindingView.answerRecyclerview.adapter = QuestionAnswerAdapter(listOfQuestions.get(index).answer)

            }else{
                bindingView.btNext.text = "Submit"
            }

        }

    }


    fun addQuestions() {

        for (i in 1..10) {

            val listOfAnswers: MutableList<Answers> = mutableListOf()

            for (j in 1..3) {
                listOfAnswers.add(Answers(i,"Answer - " + Random.nextInt(0, 100)))
            }

            listOfQuestions.add(Questions(i,"Questions - " + i,listOfAnswers))
        }

        bindingView.tQuestion.text = listOfQuestions.get(0).text
        bindingView.answerRecyclerview.adapter = QuestionAnswerAdapter(listOfQuestions.get(0).answer)

    }
}
