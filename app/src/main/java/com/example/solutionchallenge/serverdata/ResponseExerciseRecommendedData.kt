package com.example.solutionchallenge.serverdata

import com.example.solutionchallenge.datamodel.Exercise

data class ResponseExerciseRecommendedData(
    val status: String,
    val data: Exercise
)
//ExerciseData는 여기서 정의 할 필요 없음 이미 정의됨


