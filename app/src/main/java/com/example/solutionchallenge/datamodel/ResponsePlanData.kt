package com.example.solutionchallenge.datamodel

import com.google.gson.annotations.SerializedName


data class ResponsePlanData(
    val status: String,
    val message: String,
    @SerializedName("data")
    val data: Data

    )

{
    data class Data(
        val planId: Int,
        val exerciseId: Int,
        val exerciseName: String,
        val plannedTime: Int,
        val doneTime: Int
    )
}

/*
  "planId": 0,
  "exerciseId": 0,
  "exerciseName": "string",
  "plannedTime": 0,
  "doneTime": 0
  */
