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
    // private var plannedDate: String? = null
    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_dialog)

        val exerciseNames = exerciseList.map { it.name }

        var okButton: Button = findViewById(R.id.okButton)
        var cancelButton: Button = findViewById(R.id.cancelButton)
        var exerciseSpinner: Spinner = findViewById(R.id.ExerciseNameSpinner)
        var timeGoalEditView: EditText = findViewById(R.id.TimeEditView)
        var dateEditView: EditText = findViewById(R.id.DDateEditView)

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


        /*
        if (selectedDate != null) {
            // selectedDate가 null이 아닌 경우에는 DDateEditView를 보이지 않게 함
            dateEditView.visibility = View.GONE
            plannedDate = selectedDate


        } else {
            // selectedDate가 null인 경우에는 DDateEditView를 보여줘서 날짜값을 입력받게 함
            dateEditView.visibility = View.VISIBLE

        }

*/

        okButton.setOnClickListener {
            val exerciseName = selectedExerciseName
            val inputTime = timeGoalEditView.text.toString()
            val plannedTime = if (inputTime.isNotEmpty()) {
                inputTime.toInt()
            } else {
                // 사용자가 아무런 값을 입력하지 않은 경우 기본값이나 에러 처리를 수행하십시오.
                // 여기서는 0을 기본값으로 사용하도록 설정하였습니다.
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
                        // plannedDate가 null이거나 빈 문자열인 경우 처리할 내용을 여기에 추가하세요.
                        // 예를 들어, 사용자에게 날짜를 입력하도록 알리는 메시지를 표시할 수 있습니다.
                        Toast.makeText(context, "날짜를 입력해주세요.", Toast.LENGTH_SHORT).show()
                    }

                }
                dismiss()
            }
        }

        cancelButton.setOnClickListener { dismiss() }
    }
}
