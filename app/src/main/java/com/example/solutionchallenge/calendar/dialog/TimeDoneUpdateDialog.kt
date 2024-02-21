package com.example.solutionchallenge.calendar.dialog


import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
//import com.myfirstandroidapp.helpcalendar.R
import com.example.solutionchallenge.R
import com.example.solutionchallenge.ServiceCreator
import com.example.solutionchallenge.calendar.model.Plan
import com.example.solutionchallenge.datamodel.RequestPlanIdPATCHData
import com.example.solutionchallenge.datamodel.ResponsePlanIdPATCHData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TimeDoneUpdateDialog(
    context: Context,
    updateDialogInterface: UpdateDialogInterface,
    val plan: Plan,
    val rreceivedAccessToken: String
) :
    Dialog(context) {

    // 액티비티에서 인터페이스를 받아옴
    private var updateDialogInterface: UpdateDialogInterface = updateDialogInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_dialog)

        var okButton: Button = findViewById(R.id.okButton)
        var cancelButton: Button = findViewById(R.id.cancelButton)
        var timeEditView: EditText = findViewById(R.id.TimeEditView)
        val spinner: Spinner = findViewById(R.id.ExerciseNameSpinner)
        val planTimeTextView : TextView = findViewById(R.id.PlanTimeTextView)
        val diaLog : TextView = findViewById(R.id.DialogNotice)

        // 불필요한 거 숨기기
        spinner.visibility = View.GONE
        planTimeTextView.visibility = View.GONE
        diaLog.visibility = View.GONE

        window!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        okButton.setOnClickListener {
            // val exerciseName= nameEditView.text.toString()

            val doneTimeStr = timeEditView.text.toString()
            val doneTime = doneTimeStr.toIntOrNull()
            // 입력하지 않았을 때
            if (doneTime == null) {
                Toast.makeText(context, "수정할 내용을 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            }

            // 입력 창이 비어 있지 않을 때
            else {


                val requestPlanIdPATCHData = RequestPlanIdPATCHData(doneTime)

                Log.i(TAG, "RequestPlanIdPATCHData(doneTime): $doneTime")
                Log.i(TAG, "requestPlanIdPATCHData.doneTime: ${requestPlanIdPATCHData.doneTime}")


                //서버와 통신
                val callPlanPatch: Call<ResponsePlanIdPATCHData> =
                    ServiceCreator.everyHealthService.patchPlanId(
                        "Bearer $rreceivedAccessToken",
                        plan.planId,
                        requestPlanIdPATCHData
                    )
                Log.d(TAG, "Token: $rreceivedAccessToken")

                callPlanPatch.enqueue(object : Callback<ResponsePlanIdPATCHData> {
                    override fun onResponse(
                        call: Call<ResponsePlanIdPATCHData>,
                        response: Response<ResponsePlanIdPATCHData>
                    ) {
                        Log.d(TAG, "Sending PATCH request to save doneTime. (planId: ${plan.planId})")

                        if (response.isSuccessful) {
                            Log.d(TAG, "done time 전송 성공")
                            //planViewModel.deletePlan(currentPlan)
                            //ui랑 로컬데이터 업데이트
                            plan.check = true // check 값을 true로 설정


                            updateDialogInterface.onOkButtonClicked2(plan.planId, doneTime, rreceivedAccessToken)
                            dismiss()

                        } else {
                            Log.d(TAG, "done time 전송 실패")
                        }
                    }

                    override fun onFailure(
                        call: Call<ResponsePlanIdPATCHData>,
                        t: Throwable
                    ) {
                        Log.e("NetworkTest", "error:$t")
                    }


                })




            }
        }


        // 취소 버튼 클릭 시 종료
        cancelButton.setOnClickListener { dismiss() }
    }

    companion object {
        private const val TAG = "TimeDoneUpdateDialog"
    }
}