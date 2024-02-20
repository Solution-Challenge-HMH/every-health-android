package com.example.solutionchallenge.calendar

import androidx.lifecycle.LiveData
import com.example.solutionchallenge.calendar.db.PlanDao
import com.example.solutionchallenge.calendar.model.Plan
import com.example.solutionchallenge.datamodel.DatePlan
import kotlinx.coroutines.flow.Flow

class PlanRepository (private val planDao: PlanDao) {
    val readAllData : LiveData<List<Plan>> = planDao.readAllData()


    suspend fun addPlan(plan: Plan){
        planDao.addPlan(plan)
    }

    suspend fun updatePlan(plan: Plan){
        planDao.updatePlan(plan)
    }

    suspend fun deletePlan(plan: Plan){
        planDao.deletePlan(plan)
    }

    fun readDateData(year : Int, month : Int, day : Int): LiveData<List<Plan>> {
        return planDao.readDateData(year, month, day)
    }

}