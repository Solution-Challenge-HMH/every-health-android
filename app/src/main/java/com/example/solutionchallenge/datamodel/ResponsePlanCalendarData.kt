package com.example.solutionchallenge.datamodel

//PlanCalendar은 PlanToday의 List
import com.google.gson.annotations.SerializedName

//PlanCalendar은 PlanToday의 List
data class ResponsePlanCalendarData(
    val status: String,
    val message: String,
    @SerializedName("data")
    val data: List<DatePlan>
)





