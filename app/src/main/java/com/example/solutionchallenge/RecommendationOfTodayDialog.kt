package com.example.solutionchallenge

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.solutionchallenge.datamodel.ResponseExerciseExerciseIdData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RecommendationOfTodayDialog(
    context: Context,
    private val nameData: String,
    private val timeData: String,
    private val difficultyData: Int,
    private val idData: Int,
    private val tokenData: String?
) : Dialog(context) {

    private lateinit var diff1: ImageView
    private lateinit var diff2: ImageView
    private lateinit var diff3: ImageView
    private lateinit var diff4: ImageView
    private lateinit var diff5: ImageView

    @SuppressLint("ResourceAsColor", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.recommendationoftoday_dialog)

        var toCalendarButton: Button = findViewById(R.id.ToCalendarButton)
        var toRecommendationDialogButton: Button = findViewById(R.id.ToRecommendationDialogButton)
        var closeButton: Button = findViewById(R.id.CloseButton)
        var name: TextView = findViewById(R.id.RecommendNameTextView)
        var time: TextView = findViewById(R.id.RecommendTimeValue)
        // var difficulty: TextView = findViewById(R.id.RecommendDifficultyValue)

        diff1 = findViewById(R.id.diff_1)
        diff2 = findViewById(R.id.diff_2)
        diff3 = findViewById(R.id.diff_3)
        diff4 = findViewById(R.id.diff_4)
        diff5 = findViewById(R.id.diff_5)

        name.text = nameData
        time.text = timeData + "min"
        // difficulty.text = difficultyData


        window!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))


        //운동 고정 dialog로
        toCalendarButton.setOnClickListener {
            val planAddDialog =  PlanAddDialog(context, idData, tokenData)
            planAddDialog.show() }

        toRecommendationDialogButton.setOnClickListener {

            //운동 id(idData) 전달 받음
            val exerciseId = idData
            val receivedAccessToken = tokenData

            //현 운동 id로 callExerciseDetail
            val callExerciseDetail: Call<ResponseExerciseExerciseIdData> =
                ServiceCreator.everyHealthService.getExerciseExerciseId(
                    "Bearer $receivedAccessToken", exerciseId)

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
                            val recommendationDialog = RecommendationDetailDialog(context, exerciseDetail, idData, tokenData)
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
        }
        // 닫기 버튼 클릭 시 종료
        closeButton.setOnClickListener { dismiss() }

        setDifficultyViewsVisibility(difficultyData ?: 0)
    }
    private fun setDifficultyViewsVisibility(difficulty: Int) {
        diff1.visibility = if (difficulty >= 1) View.VISIBLE else View.INVISIBLE
        diff2.visibility = if (difficulty >= 2) View.VISIBLE else View.INVISIBLE
        diff3.visibility = if (difficulty >= 3) View.VISIBLE else View.INVISIBLE
        diff4.visibility = if (difficulty >= 4) View.VISIBLE else View.INVISIBLE
        diff5.visibility = if (difficulty >= 5) View.VISIBLE else View.INVISIBLE
    }
    companion object {
        private const val TAG = "RecommendationOfTodayDialog"
    }
}


