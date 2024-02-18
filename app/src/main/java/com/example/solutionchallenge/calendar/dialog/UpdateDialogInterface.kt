package com.example.solutionchallenge.calendar.dialog


interface UpdateDialogInterface {
    fun onOkButtonClicked1(exerciseId :Int,exerciseName: String,  plannedTime: Int, thisDate: String)
    fun onOkButtonClicked2(exerciseName: String, doneTime: Int, thisDate: String)
    //fun onOkButtonClicked3(name: String, time: Int, date: String)

    //fun onOkButtonClicked4(name: String, time: Int, date: String)
}