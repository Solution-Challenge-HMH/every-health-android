package com.example.solutionchallenge


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import com.example.solutionchallenge.databinding.DialogAddPlanBinding
import com.example.solutionchallenge.datamodel.RequestPlanData
import com.example.solutionchallenge.datamodel.ResponsePlanData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat

class PlanAddDialog(
    context: Context,
    idData: Int,
    tokenData: String?
    ) :
    Dialog(context) {

    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    private lateinit var thisDate: String

    val exerciseId: Int = idData
    val receivedAccessToken: String? = tokenData

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_plan)

        val okButton: Button = findViewById(R.id.okButton)
        val cancelButton: Button = findViewById(R.id.cancelButton)
        val timeGoalEditView: EditText = findViewById(R.id.timeGoalEditView)


        val calendarView: CalendarView = findViewById(R.id.calendarView2)

        calendarView.setOnDateChangeListener { _, year, month, day ->

            this.year = year
            this.month = month + 1
            this.day = day

            thisDate = "$year-${month + 1}-$day"

            Log.d(TAG, "캘린더에서 "+thisDate+" 선택됨")


        }


        okButton.setOnClickListener {
            val time_goal_Str = timeGoalEditView.text.toString()
            val time_goal_Int = time_goal_Str.toIntOrNull()

            println(time_goal_Int)
            println(thisDate)
            if (time_goal_Int == null||TextUtils.isEmpty(thisDate)) {
                Toast.makeText(context, "날째와 목표시간을 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                //백으로 데이터 넘기기
                val outputDateString = convertDateFormat(thisDate, "yyyy-M-d", "yyyy-MM-dd")
                thisDate = outputDateString
                postPlan(exerciseId, thisDate, time_goal_Int)
                dismiss()
            }


        }
        cancelButton.setOnClickListener { dismiss() }


    }


    fun postPlan(exerciseId: Int, date: String, plannedTime: Int) {
        //여기서 post plan api 호출

        val requestPlanData = RequestPlanData(
            exerciseId,
            date,
            plannedTime
        )
        val callAddPlan: Call<ResponsePlanData> =
            ServiceCreator.everyHealthService.postPlan(
                "Bearer $receivedAccessToken",
                requestPlanData
            )
        callAddPlan.enqueue(object :Callback<ResponsePlanData>{
            override fun onResponse(
                call: Call<ResponsePlanData>,
                response: Response<ResponsePlanData>
            ) {
                if (response.isSuccessful) {
                    //일정 추가하기 성공
                    Log.d(TAG, "일정 추가하기 성공")

                } else {
                    Log.d(TAG, "유저 신체 정보 전송 실패")
                }
            }

            override fun onFailure(call: Call<ResponsePlanData>, t: Throwable) {
                Log.e("NetworkTest", "error:$t")
            }
        })




    }
    fun convertDateFormat(inputDateString: String, inputFormatString: String, outputFormatString: String): String {
        val inputFormat = SimpleDateFormat(inputFormatString)
        val outputFormat = SimpleDateFormat(outputFormatString)

        val inputDate = inputFormat.parse(inputDateString) // 입력된 문자열을 날짜로 파싱
        return outputFormat.format(inputDate) // 날짜를 지정된 형식의 문자열로 변환하여 반환
    }

    companion object {
        const val TAG = "PlanAddDialog"
    }
}