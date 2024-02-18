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
import com.example.solutionchallenge.datamodel.ResponsePlanTodayData
import com.example.solutionchallenge.datamodel.ResponseUserInfoData
import com.example.solutionchallenge.datamodel.ResponseUserLoginData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path


//******* 확인할꺼: Path 전달하는 코드 맞게 짠건지 *********
interface EveryHealthService { //로그인시 사용
    @Headers("Content-Type:application/json")
    @POST("user/login")
    fun postUserLogin(@Body body: RequestUserLoginData):
            Call<ResponseUserLoginData>

    //@Headers("Authorization: Bearer ${accessToken}")
    //이 밑에있는 헤더 전부 수정해야함. 지금껀 임시 테스트용
    @Headers("Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3IiwidHlwZSI6ImFjY2VzcyIsImlhdCI6MTcwODE3NjI1OSwiZXhwIjoxNzA4MjYyNjU5fQ.ChI5GgbLUr50L-bzfmKadCRODXBAXRY-twwtF3w6Qscacw5PSuZrDkJ0o5CQyt-HWZiYun0SbctHAMIEKSpMBw")
    @POST("user/info")
    fun postUserInfo(
        @Body body: RequestUserInfoData
    ): Call<ResponseUserInfoData>


    //////Plan API
    @Headers("Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3IiwidHlwZSI6ImFjY2VzcyIsImlhdCI6MTcwODE3NjI1OSwiZXhwIjoxNzA4MjYyNjU5fQ.ChI5GgbLUr50L-bzfmKadCRODXBAXRY-twwtF3w6Qscacw5PSuZrDkJ0o5CQyt-HWZiYun0SbctHAMIEKSpMBw")
    @POST("plan")
    fun postPlan(
        @Body body: RequestPlanData
    ): Call<ResponsePlanData>

    @Headers("Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3IiwidHlwZSI6ImFjY2VzcyIsImlhdCI6MTcwODE3NjI1OSwiZXhwIjoxNzA4MjYyNjU5fQ.ChI5GgbLUr50L-bzfmKadCRODXBAXRY-twwtF3w6Qscacw5PSuZrDkJ0o5CQyt-HWZiYun0SbctHAMIEKSpMBw")
    @DELETE("plan/{planId}") //DELETE은 request body 없음
    fun deletePlanPlanId(@Path("planId") planId: Int): Call<ResponsePlanPlanidDELETEData>

    @Headers("Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3IiwidHlwZSI6ImFjY2VzcyIsImlhdCI6MTcwODE3NjI1OSwiZXhwIjoxNzA4MjYyNjU5fQ.ChI5GgbLUr50L-bzfmKadCRODXBAXRY-twwtF3w6Qscacw5PSuZrDkJ0o5CQyt-HWZiYun0SbctHAMIEKSpMBw")
    @PATCH("plan/{planId}")
    fun patchPlanPlanId(
        @Path("planId") planId: Int,
        @Field("doneTime") field: RequestPlanPlanIdPATCHData //patch 요청하는 형식 이거 아닐수도..?
    ): Call<ResponsePlanPlanidPATCHData>


    @Headers("Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3IiwidHlwZSI6ImFjY2VzcyIsImlhdCI6MTcwODE3NjI1OSwiZXhwIjoxNzA4MjYyNjU5fQ.ChI5GgbLUr50L-bzfmKadCRODXBAXRY-twwtF3w6Qscacw5PSuZrDkJ0o5CQyt-HWZiYun0SbctHAMIEKSpMBw")
    @GET("plan/today") //GET은 request body 없음
    fun getPlanToday(): Call<ResponsePlanTodayData> //path 없음

    @Headers("Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3IiwidHlwZSI6ImFjY2VzcyIsImlhdCI6MTcwODE3NjI1OSwiZXhwIjoxNzA4MjYyNjU5fQ.ChI5GgbLUr50L-bzfmKadCRODXBAXRY-twwtF3w6Qscacw5PSuZrDkJ0o5CQyt-HWZiYun0SbctHAMIEKSpMBw")
    @GET("plan/calendar") //GET은 request body 없음
    fun getPlanCalendar(): Call<List<ResponsePlanCalendarData>> //path 없음


    ////Exercise API
    @Headers("Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3IiwidHlwZSI6ImFjY2VzcyIsImlhdCI6MTcwODE3NjI1OSwiZXhwIjoxNzA4MjYyNjU5fQ.ChI5GgbLUr50L-bzfmKadCRODXBAXRY-twwtF3w6Qscacw5PSuZrDkJ0o5CQyt-HWZiYun0SbctHAMIEKSpMBw")
    @GET("exercise") //GET은 request body 없음
    fun getExercise(): Call<List<ResponseExerciseData>>

    @Headers("Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3IiwidHlwZSI6ImFjY2VzcyIsImlhdCI6MTcwODE3NjI1OSwiZXhwIjoxNzA4MjYyNjU5fQ.ChI5GgbLUr50L-bzfmKadCRODXBAXRY-twwtF3w6Qscacw5PSuZrDkJ0o5CQyt-HWZiYun0SbctHAMIEKSpMBw")
    @GET("exercise/{exerciseId}") //GET은 request body 없음
    fun getExerciseExerciseId(@Path("exerciseId") exerciseId: Int): Call<ResponseExerciseExerciseIdData> //path 있음


    @Headers("Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3IiwidHlwZSI6ImFjY2VzcyIsImlhdCI6MTcwODE3NjI1OSwiZXhwIjoxNzA4MjYyNjU5fQ.ChI5GgbLUr50L-bzfmKadCRODXBAXRY-twwtF3w6Qscacw5PSuZrDkJ0o5CQyt-HWZiYun0SbctHAMIEKSpMBw")
    @POST("exercise/{exerciseId}/bookmark") //GET에서 POST로 API 수정됨 (0217) (request body 없는 POST)
    fun postExerciseBookmark(@Path("exerciseId") exerciseId: Int): Call<ResponseExerciseBookmarkPOSTData> //path 있음
    //여기서는 Bookmark값 불필요, 항상 false로 반환됨


    @Headers("Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3IiwidHlwZSI6ImFjY2VzcyIsImlhdCI6MTcwODE3NjI1OSwiZXhwIjoxNzA4MjYyNjU5fQ.ChI5GgbLUr50L-bzfmKadCRODXBAXRY-twwtF3w6Qscacw5PSuZrDkJ0o5CQyt-HWZiYun0SbctHAMIEKSpMBw")
    @DELETE("exercise/{exerciseId}/bookmark") //DELETE은 request body 없음
    fun deleteExerciseBookmark(@Path("exerciseId") exerciseId: Int): Call<ResponseExerciseBookmarkDELETEData> //path 있음

    @Headers("Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3IiwidHlwZSI6ImFjY2VzcyIsImlhdCI6MTcwODE3NjI1OSwiZXhwIjoxNzA4MjYyNjU5fQ.ChI5GgbLUr50L-bzfmKadCRODXBAXRY-twwtF3w6Qscacw5PSuZrDkJ0o5CQyt-HWZiYun0SbctHAMIEKSpMBw")
    @GET("exercise/recommended") //GET은 request body 없음
    fun getExerciseRecommended(): Call<ResponseExerciseRecommendedData>

}

