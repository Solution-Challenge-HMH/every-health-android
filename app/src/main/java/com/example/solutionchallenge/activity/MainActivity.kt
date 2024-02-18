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
import com.example.solutionchallenge.datamodel.Exercise
import com.example.solutionchallenge.datamodel.ResponseExerciseBookmarkDELETEData
import com.example.solutionchallenge.datamodel.ResponseExerciseBookmarkPOSTData
import com.example.solutionchallenge.datamodel.ResponseExerciseData
import com.example.solutionchallenge.datamodel.ResponseExerciseExerciseIdData
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

        println("아ㅏㅏㅏ")

        val PERbutton: Button = findViewById(R.id.ToPERlistButton) //개인운동 추천 버튼
        val TodayButton: Button = findViewById(R.id.ToTodayERButton) //오늘의운동 추천 버튼
        val calendarInMain: MaterialCalendarView =
            findViewById(R.id.calendarInMain) //메인화면에서 캘린더 클릭시 캘린더로 이동x
        PERbutton.setOnClickListener {

            val call_Exercise: Call<List<ResponseExerciseData>> =
                ServiceCreator.everyHealthService.getExercise()
            call_Exercise.enqueue(object : Callback<List<ResponseExerciseData>> {
                override fun onResponse(
                    call: Call<List<ResponseExerciseData>>,
                    response: Response<List<ResponseExerciseData>> //이걸 우리쪽에서 리스트 하나에 저장하는게 낫나..?
                ) {
                    if (response.isSuccessful) {
                        //추천 운동 리스트 불러오기 성공
                        Log.d(TAG, "추천 운동 리스트 불러오기 성공")

                        //여기서.. 운동 리스트 받아서 저장........하고싶은데 ㅜㅜ

                        //val jsonArray: Array<>


                    } else {
                        Log.d(TAG, "추천 운동 리스트 불러오기 실패")
                    }
                }

                override fun onFailure(call: Call<List<ResponseExerciseData>>, t: Throwable) {
                    Log.e("NetworkTest", "error:$t")
                }
            })

            val call_ExerciseExerciseId: Call<ResponseExerciseExerciseIdData> =
                ServiceCreator.everyHealthService.getExerciseExerciseId(4) //4 임의로 넣어둔 값
            call_ExerciseExerciseId.enqueue(object : Callback<ResponseExerciseExerciseIdData> {
                override fun onResponse(
                    call: Call<ResponseExerciseExerciseIdData>,
                    response: Response<ResponseExerciseExerciseIdData>
                ) {
                    if (response.isSuccessful) {
                        //id로 운동 조회 성공
                        Log.d(TAG, "id로 운동 조회 성공")
                    } else {
                        Log.d(TAG, "id로 운동 조회 실패")
                    }
                }

                override fun onFailure(call: Call<ResponseExerciseExerciseIdData>, t: Throwable) {
                    Log.e("NetworkTest", "error:$t")
                }
            })

            val call_ExerciseBookmarkPOST: Call<ResponseExerciseBookmarkPOSTData> =
                ServiceCreator.everyHealthService.postExerciseBookmark(4) //4는 임의로 넣어둔 값
            call_ExerciseBookmarkPOST.enqueue(object : Callback<ResponseExerciseBookmarkPOSTData> {
                override fun onResponse(
                    call: Call<ResponseExerciseBookmarkPOSTData>,
                    response: Response<ResponseExerciseBookmarkPOSTData>
                ) {
                    if (response.isSuccessful) {
                        //운동 찜하기 성공
                        Log.d(TAG, "운동 찜하기 성공")
                    } else {
                        Log.d(TAG, "운동 찜하기 실패")
                    }
                }

                override fun onFailure(call: Call<ResponseExerciseBookmarkPOSTData>, t: Throwable) {
                    Log.e("NetworkTest", "error:$t")
                }
            })
            //RequestExerciseBookmarkPOSTData.kt
            val call_ExerciseBookmarkDELETE: Call<ResponseExerciseBookmarkDELETEData> =
                ServiceCreator.everyHealthService.deleteExerciseBookmark(4) //4는 임의로 넣어둔 값
            call_ExerciseBookmarkDELETE.enqueue(object :
                Callback<ResponseExerciseBookmarkDELETEData> {
                override fun onResponse(
                    call: Call<ResponseExerciseBookmarkDELETEData>,
                    response: Response<ResponseExerciseBookmarkDELETEData>
                ) {
                    if (response.isSuccessful) {
                        //운동 찜하기 취소 성공
                        Log.d(TAG, "운동 찜하기 취소 성공")
                    } else {
                        Log.d(TAG, "운동 찜하기 취소 실패")
                    }
                }

                override fun onFailure(
                    call: Call<ResponseExerciseBookmarkDELETEData>,
                    t: Throwable
                ) {
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
            lateinit var responseData: ResponseExerciseRecommendedData
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
                        responseData = response.body()!!

                    } else {
                        Log.d(TAG, "오늘의 추천 운동 불러오기 실패")
                    }
                }

                override fun onFailure(call: Call<ResponseExerciseRecommendedData>, t: Throwable) {
                    Log.e("NetworkTest", "error:$t")
                }
            })

            val randomExercise = Exercise(
                responseData.data.id,
                responseData.data.name,
                responseData.data.time,
                responseData.data.difficulty,
                responseData.data.description,
                responseData.data.caution,
                responseData.data.reference,
                responseData.data.bookmarked
            )

            // 랜덤 운동 반환하는 로직 백에서 구현해두신듯 (오늘의 추천운동 조회) -> 화면에 띄우기만 하면댐
            val dialog = RecommendationOfTodayDialog(this, randomExercise) /// 랜덤 운동 반환하는 로직 백에서 구현해두신듯 (오늘의 추천운동 조회)
            dialog.show()
        }
        calendarInMain.setOnClickListener {
            // CalendarActivity로 화면 전환... 이 안된다
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
            finish() // 현재 액티비티 종료
        }


    }


    /// 랜덤 운동 반환하는 로직 백에서 구현해두신듯 (오늘의 추천운동 조회)

    /*      private fun getRandomExercise(): Exercise {
              val randomIndex = Random.nextInt(exerciseList.size)
              return exerciseList[randomIndex]
          }
  */

    companion object {
        const val TAG = "MainActivity"
    }
}