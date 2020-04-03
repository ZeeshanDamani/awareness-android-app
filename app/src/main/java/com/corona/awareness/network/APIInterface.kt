package com.corona.awareness.network

import com.corona.awareness.model.login.loginRequest
import com.corona.awareness.model.login.loginResponse
import com.corona.awareness.model.profile.profileRequest
import com.corona.awareness.model.profile.profileResponse
import com.corona.awareness.model.questions.get_questions.questionResponse
import com.corona.awareness.model.questions.post_answers.request.postAnswerRequest
import com.corona.awareness.model.questions.post_answers.response.postAnswerResponse
import com.corona.awareness.model.record.request.recordRequest
import com.corona.awareness.model.record.response.recordResponse
import com.corona.awareness.model.servay.servayResponse
import com.corona.awareness.model.signup.signupRequest
import com.corona.awareness.model.signup.signupResponse
import retrofit2.Call
import retrofit2.http.*


interface APIInterface {


    @POST("users/signup")
    fun signupUser(@Body signupRequest: signupRequest): Call<signupResponse>


    @POST("users/login")
    fun loginUser(@Body loginRequest: loginRequest): Call<loginResponse>

    @PUT("users/{id}")
    fun profileEdit(@Path("id") userId: String,@Body profileRequest: profileRequest): Call<profileResponse>


    @GET("questionnaire/questions")
    fun  getAllQuestions(): Call<questionResponse>

    @GET("questionnaire/surveys/{id}")
    fun getAllUserServeys(@Path("id") userId : String): Call<servayResponse>


    @POST("questionnaire/response/{id}")
    fun sendQuestionAnswers(@Path("id") userId : String,
                            @Body postAnswerRequestItem: postAnswerRequest) : Call<postAnswerResponse>

    @POST("pings/{id}/record")
    fun sendUserPings(@Path(value="id", encoded=true) id : String, @Body recordRequest: recordRequest): Call<recordResponse>


    //old working style

    //    @FormUrlEncoded
//    @POST("api/v1/user/signup")
//     fun signupUser(@Field("userPhoneNumber")phone: String,
//                           @Field("firstName") firstName: String,
//                           @Field("lastName") lastName: String,
//                           @Field("userEmail") userEmail: String,
//                           @Field("userPassword") userPassword: String,
//                           @Field("dateOfBirth") dateOdBirth: String,
//                           @Field("cityId") cityId: Int,
//                           @Field("countryId") countryId: Int,
//                           @Field("cnic") cnic: String,
//                           @Field("accessType") accessType: String): Call<signupResponse>

}