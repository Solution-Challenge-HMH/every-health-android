package com.example.solutionchallenge.serverdata

import com.example.solutionchallenge.datamodel.DatePlan
import com.google.gson.annotations.SerializedName


data class ResponsePlanThisDateData(

    val status: String,
    val message: String,
    @SerializedName("data")
    val data: DatePlan

)





