package com.example.solutionchallenge

import android.util.Log
import com.example.solutionchallenge.activity.LogInActivity.Companion.TAG
import com.example.solutionchallenge.datamodel.RequestUserLoginData
import com.example.solutionchallenge.datamodel.ResponseUserLoginData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


public class TokenToServer(private val accessToken: String) {


    var member: Boolean = false // 화면 전환을 UserEdit으로 시킬지 Main으로 시킬지 판단하려고 쓰는 변수
    var receivedAccessToken : String =""

    public fun sendTokenToServer(completion: (Boolean, String?) -> Unit) {
        Log.d("TokentoServer access token ", "$accessToken")
        val requestUserLoginData = RequestUserLoginData(accessToken = accessToken)
        val call: Call<ResponseUserLoginData> =
            ServiceCreator.everyHealthService.postUserLogin(requestUserLoginData)


        call.enqueue(object : Callback<ResponseUserLoginData> {
            override fun onResponse(
                call: Call<ResponseUserLoginData>,
                response: Response<ResponseUserLoginData>
            ) {
                if (response.isSuccessful) {
                    val responseData = response.body()
                    // 서버로부터 성공적인 응답을 받은 경우 처리
                    if (responseData != null) {

                        Log.d(TAG, "Status: ${responseData.status}")
                        Log.d(TAG, "Message: ${responseData.message}")
                        Log.d(TAG, "Access Token: ${responseData.data.accessToken}")
                        Log.d(TAG, "Is Member: ${responseData.data.member}")

                        member = responseData.data.member // responseData.data.member 값을 그대로 success에 대입
                        receivedAccessToken = responseData.data.accessToken
                        completion(responseData.data.member, responseData.data.accessToken)

                    } else {
                        Log.d("NetworkTest","sth wrong")

                    }
                }
            }

            override fun onFailure(call: Call<ResponseUserLoginData>, t: Throwable) {
                // 통신 실패 시 처리
                Log.e("NetworkTest", "error:$t")

            }
        })
    }


}
