package com.myfirstandroidapp.helpcalendar.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Exercise(
    @PrimaryKey(autoGenerate = true)
    var id : Int = 0,
    val check : Boolean,
    val name : String = "운동명",
    val time_goal : Int,
    val time_done : Int,
    val year : Int,
    val month : Int,
    val day : Int
)