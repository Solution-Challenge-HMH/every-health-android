package com.example.solutionchallenge.calendar.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.solutionchallenge.calendar.model.Plan
import com.example.solutionchallenge.datamodel.DatePlan
import kotlinx.coroutines.flow.Flow

@Dao
interface PlanDao {
    // OnConflictStrategy.IGNORE = 동일한 아이디가 있을 시 무시
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPlan(plan: Plan)

    @Update
    suspend fun updatePlan(plan: Plan)

    @Delete
    suspend fun deletePlan(plan: Plan)


    //@Query("SELECT * FROM `Plan` ORDER BY thisDate DESC, planId DESC")
    @Query("SELECT * FROM `Plan` ORDER BY year DESC, month DESC, day DESC, planId DESC")
    fun readAllData() : LiveData<List<Plan>>


    // 날짜 정보를 입력받아 그 날짜에 해당하는 메모만 반환
    @Query("SELECT * FROM `Plan` WHERE year = :year AND month = :month AND day = :day ORDER BY planId DESC")
    fun readDateData(year : Int, month : Int, day : Int) : LiveData<List<Plan>>



}