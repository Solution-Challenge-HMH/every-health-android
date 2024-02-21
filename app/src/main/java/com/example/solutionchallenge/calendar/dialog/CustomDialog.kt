package com.example.solutionchallenge.calendar.dialog


import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.example.solutionchallenge.R
import com.example.solutionchallenge.calendar.dialog.CustomDialogInterface
import com.example.solutionchallenge.datamodel.Exercise

class CustomDialog(
    context: Context,
    myInterface: CustomDialogInterface,
    private val selectedDate: String?,
    private val exerciseList: List<Exercise>,
    //private val receivedAccessToken : String?
) : Dialog(context) {

    private var customDialogInterface: CustomDialogInterface = myInterface
    private var selectedExerciseName: String? = null
    private var selectedExerciseId: Int? = null
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_dialog)

        val exerciseNames = exerciseList.map { it.name }

        var okButton: Button = findViewById(R.id.okButton)
        var cancelButton: Button = findViewById(R.id.cancelButton)
        var exerciseSpinner: Spinner = findViewById(R.id.ExerciseNameSpinner)
        var timeGoalEditView: EditText = findViewById(R.id.TimeEditView)
        val doneTimeTextView: TextView = findViewById(R.id.DoneTimeTextView)

        //done time 숨기기
        doneTimeTextView.visibility = View.GONE

        val adapter = ArrayAdapter(context, android.R.layout.simple_spinner_item, exerciseNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        exerciseSpinner.adapter = adapter

        exerciseSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedExerciseName = exerciseNames[position] // 선택된 운동 이름 설정
                Log.d("ExerciseSpinner", "Selected Exercise Name: $selectedExerciseName")

                selectedExerciseId = exerciseList.find { it.name == selectedExerciseName }?.id
                Log.d("selectedId", "$selectedExerciseId")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                selectedExerciseName = null
            }
        }


        okButton.setOnClickListener {
            val exerciseName = selectedExerciseName
            val inputTime = timeGoalEditView.text.toString()
            val plannedTime = if (inputTime.isNotEmpty()) {
                inputTime.toInt()
            } else {
                0
            }

            val exerciseId = selectedExerciseId
            // plannedDate = dateEditView.text.toString() // 자리 바꿔야 함

            if (exerciseName == null || plannedTime == null  ) {
                Toast.makeText(context, "계획을 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                if (exerciseName != null) {
                    var exerciseNameString = exerciseName.toString()
                    if (!selectedDate.isNullOrBlank()) {
                        if (exerciseId != null) {
                            customDialogInterface.onOkButtonClicked1(exerciseId, exerciseNameString, plannedTime, selectedDate
                            )
                        }
                    } else {
                    }

                }
                dismiss()
            }
        }

        cancelButton.setOnClickListener { dismiss() }
    }
}
