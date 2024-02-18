package com.example.solutionchallenge.datamodel

import com.google.gson.annotations.SerializedName

data class ResponseExerciseExerciseIdData(
    val status: String,
    val message: String,
    @SerializedName("data")
    val data: Exercise
)
//ExerciseData는 여기서 정의 할 필요 없음 이미 정의됨



