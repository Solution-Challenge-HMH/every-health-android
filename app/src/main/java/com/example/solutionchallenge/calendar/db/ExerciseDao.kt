package com.myfirstandroidapp.helpcalendar.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.myfirstandroidapp.helpcalendar.model.Exercise
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

    // 큰 날짜부터 출력
    @Query("SELECT * FROM Exercise ORDER BY year DESC, month DESC, day DESC, id DESC")
    fun readAllData() : Flow<List<Exercise>>

    // 날짜 정보를 입력받아 그 날짜에 해당하는 메모만 반환
    @Query("SELECT * FROM Exercise WHERE year = :year AND month = :month AND day = :day ORDER BY id DESC")
    fun readDateData(year : Int, month : Int, day : Int) : List<Exercise>


//    @Query("SELECT * FROM Memo WHERE content LIKE :searchQuery")
//    fun searchDatabase(searchQuery : String) : Flow<List<Memo>>
}
