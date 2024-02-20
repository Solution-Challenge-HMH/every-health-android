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
import com.example.solutionchallenge.calendar.CalendarFragment
import com.example.solutionchallenge.databinding.DialogAddPlanBinding
import com.example.solutionchallenge.datamodel.RequestPlanData
import com.example.solutionchallenge.datamodel.ResponsePlanData
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
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


        val calendarView: MaterialCalendarView = findViewById(R.id.calendarView2)

        calendarView.setOnDateChangedListener {  _, date, _ ->

            val year = date.year
            val month = date.month + 1
            val day = date.day

            this.year = year
            this.month = month
            this.day = day

            //데이터 포맷 수정
            thisDate = "${this.year}-${String.format("%02d", this.month)}-${String.format("%02d", this.day)}"

            Log.d(TAG, "캘린더에서 "+thisDate+" 선택됨")


        }


        okButton.setOnClickListener {
            val timeGoalStr = timeGoalEditView.text.toString()
            val timeGoalInt = timeGoalStr.toIntOrNull()

            println(timeGoalInt)
            println(thisDate)
            if (timeGoalInt == null||TextUtils.isEmpty(thisDate)) {
                Toast.makeText(context, "Enter all your plans.", Toast.LENGTH_SHORT).show()
            } else {
               // val outputDateString = convertDateFormat(thisDate, "yyyy-M-d", "yyyy-MM-dd")
               // thisDate = outputDateString
                postPlan(exerciseId, thisDate, timeGoalInt)
                dismiss()
            }


        }
        cancelButton.setOnClickListener { dismiss() }


    }


    private fun postPlan(exerciseId: Int, date: String, plannedTime: Int) {
        //여기서 post plan api 호출

        val requestPlanData = RequestPlanData(
            exerciseId,
            date,
            plannedTime
        )
        Log.d(TAG, "RequestPlanData: $requestPlanData") // 서버로 보내기 전 확인용

        val callAddPlan: Call<ResponsePlanData> =
            ServiceCreator.everyHealthService.postPlan("Bearer $receivedAccessToken",
                requestPlanData)

        callAddPlan.enqueue(object :Callback<ResponsePlanData>{
            override fun onResponse(
                call: Call<ResponsePlanData>,
                response: Response<ResponsePlanData>
            ) {
                if (response.isSuccessful) {
                    //일정 추가하기 성공 --> 현재 서버로는 잘 보내지는데 calendar에서는 잘 안보임 (0219 민경)
                    Log.d(TAG, "플랜 전송 성공")

                } else {
                    Log.d(TAG, "플랜 전송 실패")
                }
            }

            override fun onFailure(call: Call<ResponsePlanData>, t: Throwable) {
                Log.e("NetworkTest", "error:$t")
            }
        })
    }


    companion object {
        const val TAG = "PlanAddDialog"
    }
}