package com.example.solutionchallenge.datamodel

import com.google.gson.annotations.SerializedName


data class ResponsePlanTodayData(

    val status: String,
    val message: String,
    @SerializedName("data")
    val data: DatePlan

)


/* response body 있음
[
  {
    "date": "2024-02-17",
    "planList": [
      {
        "planId": 0,
        "exerciseId": 0,
        "exerciseName": "string",
        "plannedTime": 0,
        "doneTime": 0
      }
    ]
  }
]
 */


