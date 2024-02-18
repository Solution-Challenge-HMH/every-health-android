package com.example.solutionchallenge

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log

import android.widget.Button
import android.widget.TextView
import com.example.solutionchallenge.adapter.ExerciseAdapter
import com.example.solutionchallenge.datamodel.ResponseExerciseExerciseIdData
import com.example.solutionchallenge.datamodel.ResponseExerciseRecommendedData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecommendationOfTodayDialog(
    context: Context,
    private val nameData: String,
    private val timeData: String,
    private val difficultyData: String,
    private val idData: Int,
    private val tokenData: String?
) : Dialog(context) {

//class RecommendationOfTodayDialog(context: Context, private val nameData:String, private val timeData:String, private val difficultyData:String) : Dialog(context) {

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recommendationoftoday_dialog)

        var toCalendarButton: Button = findViewById(R.id.ToCalendarButton)
        var closeButton: Button = findViewById(R.id.CloseButton)
        var name: TextView = findViewById(R.id.RecommendNameTextView)
        var time: TextView = findViewById(R.id.RecommendTimeValue)
        var difficulty: TextView = findViewById(R.id.RecommendDifficultyValue)


        name.text = nameData
        time.text = timeData
        difficulty.text = difficultyData


        window!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))


        // 닫기 버튼 클릭 시 종료
        closeButton.setOnClickListener { dismiss() }


        val toRecommendationDialogButton: Button = findViewById(R.id.ToRecommendationDialogButton)
        toRecommendationDialogButton.setOnClickListener {


            //얘가 클릭되면, 운동 상세 정보 화면 띄우는 액티비티로 넘어가야함.

            //운동 id(idData) 전달 받아서 -> done
            val exerciseId = idData
            val receivedAccessToken = tokenData

            //그 id로 callExerciseDetail 한번 한다음에,
            // 운동의 상세 정보를 가져오기 위해 서버로 요청
            val callExerciseDetail: Call<ResponseExerciseExerciseIdData> =
                ServiceCreator.everyHealthService.getExerciseExerciseId(
                    "Bearer $receivedAccessToken",
                    exerciseId
                )

            callExerciseDetail.enqueue(object : Callback<ResponseExerciseExerciseIdData> {
                override fun onResponse(
                    call: Call<ResponseExerciseExerciseIdData>,
                    response: Response<ResponseExerciseExerciseIdData>
                ) {
                    if (response.isSuccessful) {
                        val responseExerciseDetailData = response.body()
                        if (responseExerciseDetailData != null) {
                            val exerciseDetail = responseExerciseDetailData.data
                            // 운동의 상세 정보를 사용하여 다이얼로그를 띄우는 등의 작업 수행
                            val recommendationDialog =
                                RecommendationDetailDialog(context, exerciseDetail)
                                //RecommendationDetailDialog(binding.root.context, exerciseDetail)
                            recommendationDialog.show()
                        } else {
                            Log.d(TAG, "운동 상세 정보 없음")
                        }
                    } else {
                        Log.d(TAG, "운동 상세 정보 가져오기 실패")
                    }
                }

                override fun onFailure(call: Call<ResponseExerciseExerciseIdData>, t: Throwable) {
                    Log.e(TAG, "운동 상세 정보 요청 실패: $t")
                }
            })
/*
            //RecommendationDetailDialog로 전환
            val recommendationDialog = RecommendationDetailDialog(context, exercise)
            recommendationDialog.show()*/
        }
    }

    companion object {
        private const val TAG = "ExerciseAdapter"
    }
}


