package com.example.solutionchallenge.datamodel

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class DatePlan(
    @PrimaryKey
    val date : String, //이거 date형으로 하면 Thu Feb 01 00:00:00 GMT+09:00 2024 이렇게 가져와짐
    var planList : MutableList<Plan>
)