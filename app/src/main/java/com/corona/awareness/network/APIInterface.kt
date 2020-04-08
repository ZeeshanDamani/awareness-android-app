package com.corona.awareness.network

import com.corona.awareness.model.City
import com.corona.awareness.model.PasswordUpdaterRequestModel
import com.corona.awareness.model.login.LoginRequestModel
import com.corona.awareness.model.login.LoginResponseModel
import com.corona.awareness.model.profile.ProfileRequestModel
import com.corona.awareness.model.questions.get_questions.QuestionResponseModel
import com.corona.awareness.model.questions.post_answers.request.PostAnswerRequestModel
import com.corona.awareness.model.questions.post_answers.response.PostAnswerResponseModel
import com.corona.awareness.model.record.request.PingRequestModel
import com.corona.awareness.model.record.response.PingResponseModel
import com.corona.awareness.model.servay.SurveyResponseModel
import com.corona.awareness.model.signup.SignUpRequestModel
import com.corona.awareness.model.signup.SignUpResponseModel
import retrofit2.Call
import retrofit2.http.*


interface APIInterface {

    @POST("users/signup")
    fun signUpUser(@Body signUpRequestModel: SignUpRequestModel): Call<SignUpResponseModel>


    @POST("users/login")
    fun loginUser(@Body loginRequest: LoginRequestModel): Call<LoginResponseModel>

    @PUT("users/{id}")
    fun profileEdit(
        @Path("id") userId: String, @Body profileRequest: ProfileRequestModel
    ): Call<LoginResponseModel.User>

        @POST("password/{userId}")
    fun updatePassword(@Path("userId") userId: String,
                       @Body passwordUpdaterRequestModel: PasswordUpdaterRequestModel): Call<Void>

    @GET("questionnaire/questions")
    fun getAllQuestions(): Call<QuestionResponseModel>

    @GET("questionnaire/surveys/{id}")
    fun getUserSurveys(@Path("id") userId: String): Call<SurveyResponseModel>

    @GET("city")
    fun getCities(): Call<List<City>>


    @POST("questionnaire/response/{id}")
    fun sendQuestionAnswers(
        @Path("id") userId: String,
        @Body postAnswerRequestItem: PostAnswerRequestModel
    ): Call<PostAnswerResponseModel>

    @POST("pings/{id}/record")
    fun sendUserPings(
        @Path(value = "id", encoded = true) id: String, @Body recordRequest: PingRequestModel
    ): Call<PingResponseModel>
}