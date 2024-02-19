package com.example.solutionchallenge

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle

import android.widget.Button
import android.widget.TextView
import com.example.solutionchallenge.calendar.dialog.CustomDialog
import com.example.solutionchallenge.calendar.dialog.CustomDialogInterface
import com.example.solutionchallenge.datamodel.Exercise



class RecommendationDetailDialog(context: Context, private val exercise: Exercise) : Dialog(context),
    CustomDialogInterface {

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recommendation_detail_dialog)

        var toCalendarButton: Button = findViewById(R.id.ToCalendarButton)
        var closeButton: Button = findViewById(R.id.CloseButton)
        var name: TextView = findViewById(R.id.RecommendNameTextView)
        var time: TextView = findViewById(R.id.RecommendTimeValue)
        var difficulty: TextView = findViewById(R.id.RecommendDifficultyValue)
        var description: TextView = findViewById(R.id.RecommendDescriptionValue)
        var caution: TextView = findViewById(R.id.RecommendCautionValue)
        var reference: TextView = findViewById(R.id.RecommendReferenceValue)

        name.text = exercise.name
        time.text = exercise.time.toString()
        difficulty.text = exercise.difficulty.toString()
        description.text = exercise.description
        caution.text = exercise.caution
        reference.text = exercise.reference

        window!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))

        toCalendarButton.setOnClickListener {
            val exerciseList = mutableListOf(exercise)
            val customDialog = CustomDialog(context, this@RecommendationDetailDialog as CustomDialogInterface, null, exerciseList )
            customDialog.show()
            dismiss()
        }

        // 닫기 버튼 클릭 시 다이얼로그 종료
        closeButton.setOnClickListener { dismiss() }
    }

    // CustomDialogInterface의 메서드 구현
    override fun onOkButtonClicked1(exerciseId: Int, exerciseName: String, plannedTime: Int, thisDate: String) {

    }

}