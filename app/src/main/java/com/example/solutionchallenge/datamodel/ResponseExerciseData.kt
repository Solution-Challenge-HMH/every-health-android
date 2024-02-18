package com.example.solutionchallenge.datamodel

import com.google.gson.annotations.SerializedName

data class ResponseExerciseData(
    val status: String,
    val message: String,
    @SerializedName("data")
    val data: List<Exercise>
)

