package com.example.solutionchallenge.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.solutionchallenge.datamodel.Exercise

@Database(entities = [Exercise::class], version = 2, exportSchema = false)
abstract class ExerciseDatabase : RoomDatabase(){

    abstract fun exerciseDao() : ExerciseDao

    companion object{
        //thread가 직접 main memory에 접근
        @Volatile
        private var instance : ExerciseDatabase? = null

        // 싱글톤으로 생성
        fun getDatabase(context : Context) : ExerciseDatabase? {
            if(instance == null){
                synchronized(ExerciseDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ExerciseDatabase::class.java,
                        "exercise_database"
                    ).build()
                }
            }
            return instance
        }
    }
}