package com.example.solutionchallenge.activity

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.solutionchallenge.R
import com.example.solutionchallenge.RecommendationOfTodayDialog
import com.example.solutionchallenge.ServiceCreator
import com.example.solutionchallenge.datamodel.ResponseExerciseData
import com.example.solutionchallenge.datamodel.ResponseExerciseRecommendedData
import com.example.solutionchallenge.fragment.RecommendListFragment

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response





class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val PERbutton: Button = findViewById(R.id.ToPERlistButton) //개인운동 추천 버튼
        val todayButton: Button = findViewById(R.id.ToTodayERButton) //오늘의운동 추천 버튼
        val toCalendarButton: Button =
            findViewById(R.id.ToCalendarButtonInMain)
        val receivedAccessToken = intent.getStringExtra("receivedAccessToken")

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbarButton(toolbar, receivedAccessToken)


        PERbutton.setOnClickListener {
            Log.d("token", "$receivedAccessToken")
            val callExercise: Call<ResponseExerciseData> =
                ServiceCreator.everyHealthService.getExercise("Bearer $receivedAccessToken")

            callExercise.enqueue(object : Callback<ResponseExerciseData> {
                override fun onResponse(
                    call: Call<ResponseExerciseData>,
                    response: Response<ResponseExerciseData>
                ) {
                    if (response.isSuccessful) {
                        val responseExerciseData = response.body()

                        if (responseExerciseData != null) {
                            val exerciseList = responseExerciseData.data
                            val parcelableExerciseList = ArrayList<Parcelable>(exerciseList.size)

                            exerciseList.forEach { exercise ->
                                parcelableExerciseList.add(exercise)
                                Log.d("Parceling Exercise", exercise.toString())

                                val fragment = receivedAccessToken?.let { it1 ->
                                    RecommendListFragment(it1).apply {
                                        arguments = Bundle().apply {
                                            // 운동 목록 데이터를 Bundle에 넣어 전달
                                            putParcelableArrayList("exerciseList", parcelableExerciseList)

                                        }
                                    }
                                }

                                if (fragment != null) {
                                    supportFragmentManager.beginTransaction()
                                        .replace(R.id.fragment_container, fragment)
                                        .addToBackStack(null) // 백 스택에 추가
                                        .commit()
                                }


                            }

                        } else {
                            Log.d(TAG, "운동 데이터 없음")
                        }
                    } else {
                        Log.d(TAG, "운동 데이터 가져오기 실패")
                    }
                }

                override fun onFailure(call: Call<ResponseExerciseData>, t: Throwable) {
                    Log.e(TAG, "네트워크 요청 실패: $t")
                }
            })

        }
        todayButton.setOnClickListener {
            val callExerciseRecommended: Call<ResponseExerciseRecommendedData> =
                ServiceCreator.everyHealthService.getExerciseRecommended("Bearer $receivedAccessToken")
            callExerciseRecommended.enqueue(object : Callback<ResponseExerciseRecommendedData> {
                override fun onResponse(
                    call: Call<ResponseExerciseRecommendedData>,
                    response: Response<ResponseExerciseRecommendedData>
                ) {
                    if (response.isSuccessful) {
                        //오늘의 추천 운동 불러오기 성공
                        Log.d(TAG, "오늘의 추천 운동 불러오기 성공")
                        val responseData = response.body()!!.data
                        ToRecommendationDialog(
                            responseData.name,
                            responseData.time.toString(),
                            responseData.difficulty,
                            responseData.id,
                            receivedAccessToken
                        )

                    } else {
                        Log.d(TAG, "오늘의 추천 운동 불러오기 실패")
                    }
                }

                override fun onFailure(call: Call<ResponseExerciseRecommendedData>, t: Throwable) {
                    Log.e("NetworkTest", "error:$t")

                }
            })


        }
        toCalendarButton.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            intent.putExtra("receivedAccessToken", receivedAccessToken)
            startActivity(intent)
            finish() // 현재 액티비티 종료
        }

    }

    fun ToRecommendationDialog(nameData: String, timeData: String, difficultyData: Int, idData: Int, tokenData: String?) {
        val dialog = RecommendationOfTodayDialog(this,nameData,timeData,difficultyData, idData, tokenData) /// 랜덤 운동 반환하는 로직 백에서 구현해두신듯 (오늘의 추천운동 조회)
        dialog.show()
    }

    /*
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbarButton(toolbar)
     */
    fun toolbarButton(toolbar: androidx.appcompat.widget.Toolbar, receivedAccessToken: String?){

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.ToCalendarButtonInMenu -> { // 메뉴 아이템의 ID에 따라 동작을 결정합니다.
                    // 다음 activity로 이동하는 코드를 작성합니다.
                    val intent = Intent(this, CalendarActivity::class.java)
                    intent.putExtra("receivedAccessToken", receivedAccessToken)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.ToMypageButtonInMenu -> { // 메뉴 아이템의 ID에 따라 동작을 결정합니다.
                    // 다음 activity로 이동하는 코드를 작성합니다.
                    val intent = Intent(this, LogOutActivity::class.java)
                    intent.putExtra("receivedAccessToken", receivedAccessToken)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.ToMainButtonInMenu -> { // 메뉴 아이템의 ID에 따라 동작을 결정합니다.
                    // 다음 activity로 이동하는 코드를 작성합니다.
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("receivedAccessToken", receivedAccessToken)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }
    }


    companion object {
        const val TAG = "MainActivity"
    }
}