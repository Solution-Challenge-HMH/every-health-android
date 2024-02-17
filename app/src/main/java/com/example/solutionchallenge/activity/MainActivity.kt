package com.example.solutionchallenge.activity

//import com.example.solutionchallenge.datamodel.exerciseList
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.solutionchallenge.R
import com.example.solutionchallenge.ServiceCreator
import com.example.solutionchallenge.datamodel.ResponseExerciseData
import com.example.solutionchallenge.datamodel.ResponseExerciseRecommendedData
import com.example.solutionchallenge.fragment.RecommendListFragment
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val PERbutton: Button = findViewById(R.id.ToPERlistButton) //개인운동 추천 버튼
        val TodayButton: Button = findViewById(R.id.ToTodayERButton) //오늘의운동 추천 버튼
        val calendarInMain: MaterialCalendarView =
            findViewById(R.id.calendarInMain) //메인화면에서 캘린더 클릭시 캘린더로 이동x
        PERbutton.setOnClickListener {
            //이걸 우리쪽에서 리스트 하나에 저장하는게 낫나..?
            val call_Exercise: Call<List<ResponseExerciseData>> =
                ServiceCreator.everyHealthService.getExercise()
            call_Exercise.enqueue(object : Callback<List<ResponseExerciseData>> {
                override fun onResponse(
                    call: Call<List<ResponseExerciseData>>,
                    response: Response<List<ResponseExerciseData>>
                ) {
                    if (response.isSuccessful) {
                        //추천 운동 리스트 불러오기 성공
                        Log.d(TAG, "추천 운동 리스트 불러오기 성공")
                    } else {
                        Log.d(TAG, "추천 운동 리스트 불러오기 실패")
                    }
                }

                override fun onFailure(call: Call<List<ResponseExerciseData>>, t: Throwable) {
                    Log.e("NetworkTest", "error:$t")
                }
            })

            val fragment = RecommendListFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null) // 백 스택에 추가
                .commit()
        }

        TodayButton.setOnClickListener {
            val call_ExerciseRecommended: Call<ResponseExerciseRecommendedData> =
                ServiceCreator.everyHealthService.getExerciseRecommended()
            call_ExerciseRecommended.enqueue(object : Callback<ResponseExerciseRecommendedData> {
                override fun onResponse(
                    call: Call<ResponseExerciseRecommendedData>,
                    response: Response<ResponseExerciseRecommendedData>
                ) {
                    if (response.isSuccessful) {
                        //오늘의 추천 운동 불러오기 성공
                        Log.d(TAG, "오늘의 추천 운동 불러오기 성공")
                    } else {
                        Log.d(TAG, "오늘의 추천 운동 불러오기 실패")
                    }
                }

                override fun onFailure(call: Call<ResponseExerciseRecommendedData>, t: Throwable) {
                    Log.e("NetworkTest", "error:$t")
                }
            })
            //val randomExercise = getRandomExercise()
            // / 랜덤 운동 반환하는 로직 백에서 구현해두신듯 (오늘의 추천운동 조회)
            //val dialog = RecommendationOfTodayDialog(this, randomExercise) /// 랜덤 운동 반환하는 로직 백에서 구현해두신듯 (오늘의 추천운동 조회)
            //dialog.show()
        }
        calendarInMain.setOnClickListener {
            // CalendarActivity로 화면 전환... 이 안된다
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
            finish() // 현재 액티비티 종료
        }


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