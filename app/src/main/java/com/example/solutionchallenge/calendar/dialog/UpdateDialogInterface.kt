package com.example.solutionchallenge.calendar.dialog


interface UpdateDialogInterface {
    fun onOkButtonClicked1(
        exerciseId: Int,
        exerciseName: String,
        plannedTime: Int,
        thisDate: String
    )

    fun onOkButtonClicked2(
        planId: Int,
        doneTime: Int,
        receivedAccessToken: String
    )
    //fun onOkButtonClicked3(name: String, time: Int, date: String)

    //fun onOkButtonClicked4(name: String, time: Int, date: String)
}