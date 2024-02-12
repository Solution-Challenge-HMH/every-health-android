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


class CustomDialog(context : Context, myInterface: CustomDialogInterface) : Dialog(context) {

    // 액티비티에서 인터페이스를 받아옴
    private var customDialogInterface: CustomDialogInterface = myInterface

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_dialog)

        var okButton : Button = findViewById(R.id.okButton)
        var cancelButton : Button = findViewById(R.id.cancelButton)
        var nameEditView : EditText = findViewById(R.id.NameEditView)
        var timeEditView : EditText = findViewById(R.id.TimeEditView)

        // 배경 투명하게 바꿔줌
       // window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        okButton.setOnClickListener {
            val name = nameEditView.text.toString()
            val timeStr = timeEditView.text.toString()
            val time = timeStr.toIntOrNull()

            // 입력하지 않았을 때
            if (TextUtils.isEmpty(name) || time == null) {
                Toast.makeText(context, "이름과 시간을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                // 이름과 시간을 추가해줌
                customDialogInterface.onOkButtonClicked1(name, time)
                dismiss()
            }
        }


        // 취소 버튼 클릭 시 종료
        cancelButton.setOnClickListener { dismiss()}
    }
}