package com.example.solutionchallenge.repository

import com.example.solutionchallenge.db.ExerciseDao
import com.example.solutionchallenge.datamodel.Exercise
import kotlinx.coroutines.flow.Flow

class ExerciseRepository (private val exerciseDao: ExerciseDao) {
    val readAllData : Flow<List<Exercise>> = exerciseDao.readAllData()
    val readBookmarkData : Flow<List<Exercise>> = exerciseDao.readBookmarkData()


    suspend fun addExercise(exercise: Exercise){
        exerciseDao.addExercise(exercise)
    }

    suspend fun updateExercise(exercise: Exercise){
        exerciseDao.updateExercise(exercise)
    }
    suspend fun deleteExercise(exercise: Exercise){
        exerciseDao.deleteExercise(exercise)
    }

}