package com.myfirstandroidapp.helpcalendar.dialog


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
//import com.myfirstandroidapp.helpcalendar.R
import com.example.solutionchallenge.R
import com.example.solutionchallenge.calendar.dialog.CustomDialogInterface


class CustomDialog(context: Context, myInterface: CustomDialogInterface, selected_date: String) :
    Dialog(context) {

    // 액티비티에서 인터페이스를 받아옴
    private var customDialogInterface: CustomDialogInterface = myInterface

    val thisDate = selected_date
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_dialog)

        var okButton: Button = findViewById(R.id.okButton)
        var cancelButton: Button = findViewById(R.id.cancelButton)
        var exerciseEditView: EditText = findViewById(R.id.NameEditView)
        var timeGoalEditView: EditText = findViewById(R.id.TimeEditView)
        //var dateEditView: EditText = findViewById(R.id.dateEditView)


        // 배경 투명하게 바꿔줌
        // window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        okButton.setOnClickListener {
            val exerciseName = exerciseEditView.text.toString()
            val time_goal_Str = timeGoalEditView.text.toString()
            val time_goal_Int = time_goal_Str.toIntOrNull()
            // 입력하지 않았을 때


            if (TextUtils.isEmpty(exerciseName) || time_goal_Int == null) {
                Toast.makeText(context, "이름과 시간을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                // 이름과 시간을 추가해줌
                customDialogInterface.onOkButtonClicked1(exerciseName, time_goal_Int, thisDate)
                dismiss()
            }
        }


        // 취소 버튼 클릭 시 종료
        cancelButton.setOnClickListener { dismiss() }
    }
}