package com.myfirstandroidapp.helpcalendar

import com.myfirstandroidapp.helpcalendar.db.ExerciseDao
import com.myfirstandroidapp.helpcalendar.model.Exercise
import kotlinx.coroutines.flow.Flow

class ExerciseRepository (private val exerciseDao: ExerciseDao) {
    val readAllData : Flow<List<Exercise>> = exerciseDao.readAllData()

    suspend fun addExercise(exercise: Exercise){
        exerciseDao.addExercise(exercise)
    }

    suspend fun updateExercise(exercise: Exercise){
        exerciseDao.updateExercise(exercise)
    }

    suspend fun deleteExercise(exercise: Exercise){
        exerciseDao.deleteExercise(exercise)
    }

    fun readDateData(year : Int, month : Int, day : Int): List<Exercise> {
        return exerciseDao.readDateData(year, month, day)
    }

}