package com.corona.awareness.network

import com.corona.awareness.network.model.*
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
    ): Call<User>

    @PUT("users/password/{userId}")
    fun updatePassword(@Path("userId") userId: String,
                       @Body passwordUpdaterRequestModel: PasswordUpdateRequestModel
    ): Call<Void>

    @GET("questionnaire/questions")
    fun getAllQuestions(): Call<QuestionResponseModel>

    @GET("questionnaire/surveys/{id}")
    fun getUserSurveys(@Path("id") userId: String): Call<SurveyResponseModel>

    @GET("city")
    fun getCities(): Call<List<City>>


    @POST("questionnaire/response/{id}")
    fun sendQuestionAnswers(
        @Path("id") userId: Int,
        @Body postAnswerRequestItem: PostAnswerRequestModel
    ): Call<DiagnosticResult>

    @POST("pings/{id}/record")
    fun sendUserPings(
        @Path(value = "id", encoded = true) id: String, @Body recordRequest: PingRequestModel
    ): Call<PingResponseModel>
}