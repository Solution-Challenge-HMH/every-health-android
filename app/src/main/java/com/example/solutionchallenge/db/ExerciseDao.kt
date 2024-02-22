package com.example.solutionchallenge.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.solutionchallenge.datamodel.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    // OnConflictStrategy.IGNORE = 동일한 아이디가 있을 시 무시
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addExercise(exercise: Exercise)

    @Update
    suspend fun updateExercise(exercise: Exercise)

    @Delete
    suspend fun deleteExercise(exercise: Exercise)

    @Query("SELECT * FROM Exercise ORDER BY id DESC")
    fun readAllData(): Flow<List<Exercise>>

    //즐겨찾기된 목록만 출력
    @Query("SELECT * FROM Exercise WHERE `bookmarked` = 1 ORDER BY  id DESC")
    fun readBookmarkData(): Flow<List<Exercise>>

}


