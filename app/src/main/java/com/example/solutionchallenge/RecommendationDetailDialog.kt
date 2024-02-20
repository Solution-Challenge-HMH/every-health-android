package com.example.solutionchallenge

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View

import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.solutionchallenge.calendar.dialog.CustomDialog
import com.example.solutionchallenge.calendar.dialog.CustomDialogInterface
import com.example.solutionchallenge.datamodel.Exercise


class RecommendationDetailDialog(
    context: Context,
    private val exercise : Exercise,
    private val idData: Int,
    private val tokenData: String?
) : Dialog(context),
    CustomDialogInterface {

    private lateinit var diff1: ImageView
    private lateinit var diff2: ImageView
    private lateinit var diff3: ImageView
    private lateinit var diff4: ImageView
    private lateinit var diff5: ImageView

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.recommendation_detail_dialog)

        var toCalendarButton: Button = findViewById(R.id.ToCalendarButton)
        var closeButton: Button = findViewById(R.id.CloseButton)
        var name: TextView = findViewById(R.id.RecommendNameTextView)
        var time: TextView = findViewById(R.id.RecommendTimeValue)
        //var difficulty: TextView = findViewById(R.id.RecommendDifficultyValue)
        var description: TextView = findViewById(R.id.RecommendDescriptionValue)
        var caution: TextView = findViewById(R.id.RecommendCautionValue)
        var reference: TextView = findViewById(R.id.RecommendReferenceValue)

        diff1 = findViewById(R.id.diff_1)
        diff2 = findViewById(R.id.diff_2)
        diff3 = findViewById(R.id.diff_3)
        diff4 = findViewById(R.id.diff_4)
        diff5 = findViewById(R.id.diff_5)

        name.text = exercise.name
        time.text = exercise.time.toString()+" min"
        // difficulty.text = exercise.difficulty.toString()
        description.text = exercise.description
        caution.text = exercise.caution
        reference.text = exercise.reference

        window!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))


        //운동 고정 dialog로
        toCalendarButton.setOnClickListener {
            val planAddDialog = PlanAddDialog(context, idData, tokenData)
            planAddDialog.show()
        }

        // 닫기 버튼 클릭 시 다이얼로그 종료
        closeButton.setOnClickListener { dismiss() }

        setDifficultyViewsVisibility(exercise.difficulty ?: 0)

    }
    private fun setDifficultyViewsVisibility(difficulty: Int) {
        diff1.visibility = if (difficulty >= 1) View.VISIBLE else View.INVISIBLE
        diff2.visibility = if (difficulty >= 2) View.VISIBLE else View.INVISIBLE
        diff3.visibility = if (difficulty >= 3) View.VISIBLE else View.INVISIBLE
        diff4.visibility = if (difficulty >= 4) View.VISIBLE else View.INVISIBLE
        diff5.visibility = if (difficulty >= 5) View.VISIBLE else View.INVISIBLE
    }

    // CustomDialogInterface의 메서드 구현
    override fun onOkButtonClicked1(
        exerciseId: Int,
        exerciseName: String,
        plannedTime: Int,
        thisDate: String
    ) {

    }

}