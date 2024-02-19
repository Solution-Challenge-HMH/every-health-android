package com.example.solutionchallenge

import com.example.solutionchallenge.datamodel.RequestPlanData
import com.example.solutionchallenge.datamodel.RequestPlanPlanIdPATCHData
import com.example.solutionchallenge.datamodel.RequestUserInfoData
import com.example.solutionchallenge.datamodel.RequestUserLoginData
import com.example.solutionchallenge.datamodel.ResponseExerciseBookmarkDELETEData
import com.example.solutionchallenge.datamodel.ResponseExerciseBookmarkPOSTData
import com.example.solutionchallenge.datamodel.ResponseExerciseData
import com.example.solutionchallenge.datamodel.ResponseExerciseExerciseIdData
import com.example.solutionchallenge.datamodel.ResponseExerciseRecommendedData
import com.example.solutionchallenge.datamodel.ResponsePlanCalendarData
import com.example.solutionchallenge.datamodel.ResponsePlanData
import com.example.solutionchallenge.datamodel.ResponsePlanPlanidDELETEData
import com.example.solutionchallenge.datamodel.ResponsePlanPlanidPATCHData
import com.example.solutionchallenge.datamodel.ResponsePlanThisDateData
import com.example.solutionchallenge.datamodel.ResponsePlanTodayData
import com.example.solutionchallenge.datamodel.ResponseUserInfoData
import com.example.solutionchallenge.datamodel.ResponseUserLoginData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query


//******* 확인할꺼: Path 전달하는 코드 맞게 짠건지 *********
interface EveryHealthService { //로그인시 사용
    @Headers("Content-Type:application/json")
    @POST("user/login")
    fun postUserLogin(@Body body: RequestUserLoginData):
            Call<ResponseUserLoginData>

    //@Headers("Authorization: Bearer ${accessToken}")
    //이 밑에있는 헤더 전부 수정해야함. 지금껀 임시 테스트용
    @POST("user/info")
    fun postUserInfo(
        @Header("Authorization") authorization: String,
        @Body body: RequestUserInfoData
    ): Call<ResponseUserInfoData>


    //////Plan API
    @POST("plan")
    fun postPlan(
        @Header("Authorization") authorization: String,
        @Body body: RequestPlanData
    ): Call<ResponsePlanData>

    @DELETE("plan/{planId}") //DELETE은 request body 없음
    fun deletePlanPlanId(
        @Header("Authorization") authorization: String,
        @Path("planId") planId: Int,
    ): Call<ResponsePlanPlanidDELETEData>


    @PATCH("plan/{planId}")
    fun patchPlanPlanId(
        @Header("Authorization") authorization: String,
        @Path("planId") planId: Int,
        @Field("doneTime") field: RequestPlanPlanIdPATCHData //patch 요청하는 형식 이거 아닐수도..?
    ): Call<ResponsePlanPlanidPATCHData>

    @GET("plan") //GET은 request body 없음
    fun getPlanOfThisDate(
        @Header("Authorization") authorization: String,
        @Query("date") date: String // 날짜별 플랜 조회를 위한 쿼리 추가, 형식은 ?date={}
    ): Call<ResponsePlanThisDateData> //path 없음

    @GET("plan") //GET은 request body 없음
    fun getPlanToday(
        @Header("Authorization") authorization: String,
    ): Call<ResponsePlanTodayData> //path 없음

    @GET("plan/calendar") //GET은 request body 없음
    fun getPlanCalendar(
        @Header("Authorization") authorization: String,
    ): Call<ResponsePlanCalendarData> //path 없음


    ////Exercise API
    @GET("exercise") //GET은 request body 없음
    fun getExercise(
        @Header("Authorization") authorization: String
    ): Call<ResponseExerciseData>

    @GET("exercise/{exerciseId}") //GET은 request body 없음

    fun getExerciseExerciseId(@Header("Authorization") authorization: String,
                              @Path("exerciseId") exerciseId: Int
    ): Call<ResponseExerciseExerciseIdData> //path 있음




    @POST("exercise/{exerciseId}/bookmark") //GET에서 POST로 API 수정됨 (0217) (request body 없는 POST)
    fun postExerciseExerciseIdBookmark(
        @Header("Authorization") authorization: String,
        @Path("exerciseId") exerciseId: Int
    ): Call<ResponseExerciseBookmarkPOSTData> //path 있음


    @DELETE("exercise/{exerciseId}/bookmark") //DELETE은 request body 없음
    fun deleteExerciseExerciseIdBookmark(
        @Header("Authorization") authorization: String,
        @Path("exerciseId") exerciseId: Int,
    ): Call<ResponseExerciseBookmarkDELETEData> //path 있음

    @GET("exercise/recommended") //GET은 request body 없음
    fun getExerciseRecommended(
        @Header("Authorization") authorization: String,
    ): Call<ResponseExerciseRecommendedData>

}

