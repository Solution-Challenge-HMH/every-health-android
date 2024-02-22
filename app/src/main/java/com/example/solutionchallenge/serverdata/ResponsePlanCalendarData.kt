package com.example.solutionchallenge.serverdata

//PlanCalendar은 PlanToday의 List
import com.example.solutionchallenge.datamodel.DatePlan
import com.google.gson.annotations.SerializedName

//PlanCalendar은 PlanToday의 List
data class ResponsePlanCalendarData(
    val status: String,
    val message: String,
    @SerializedName("data")
    val data: List<DatePlan>
)





