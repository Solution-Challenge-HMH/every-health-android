package com.example.solutionchallenge.activity

//import com.example.solutionchallenge.datamodel.exerciseList
import android.content.Intent
import android.os.Bundle
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
            findViewById(R.id.ToCalendarButtonInMain) //메인화면에서 캘린더 클릭시 캘린더로 이동x
        val receivedAccessToken = intent.getStringExtra("receivedAccessToken")

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
                            exerciseList.forEach { exercise ->
                                val name = exercise.name
                                Log.d("Exercise Id", "${exercise.id}")
                                Log.d("Exercise Name", name)
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

            val fragment = RecommendListFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null) // 백 스택에 추가
                .commit()
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
                        val responseData = response.body()!!.data //nonnull? safe?
                        ToRecommendationDialog(
                            responseData.name,
                            responseData.time.toString(),
                            responseData.difficulty.toString()
                        )

                    } else {
                        Log.d(TAG, "오늘의 추천 운동 불러오기 실패")
                    }
                }

                override fun onFailure(call: Call<ResponseExerciseRecommendedData>, t: Throwable) {
                    Log.e("NetworkTest", "error:$t") //여기서 문젠데...

                }
            })
            // val randomExercise = getRandomExercise()
            // 랜덤 운동 반환하는 로직 백에서 구현해두신듯 (오늘의 추천운동 조회)

        }
        toCalendarButton.setOnClickListener {
            // CalendarActivity로 화면 전환... 이 안된다
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
            finish() // 현재 액티비티 종료
        }


    }

    fun ToRecommendationDialog(nameData: String, timeData: String, difficultyData: String) {
        val dialog = RecommendationOfTodayDialog(this,nameData,timeData,difficultyData) /// 랜덤 운동 반환하는 로직 백에서 구현해두신듯 (오늘의 추천운동 조회)
        dialog.show()
    }

    /// 랜덤 운동 반환하는 로직 백에서 구현해두신듯 (오늘의 추천운동 조회)
    /*
        private fun getRandomExercise(): Exercise {
            val randomIndex = Random.nextInt(exerciseList.size)
            return exerciseList[randomIndex]
        }
    */

    companion object {
        const val TAG = "MainActivity"
    }
}