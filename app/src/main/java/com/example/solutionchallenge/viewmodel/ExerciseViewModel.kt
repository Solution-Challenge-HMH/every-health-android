package com.example.solutionchallenge.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.solutionchallenge.db.ExerciseDatabase
import com.example.solutionchallenge.repository.ExerciseRepository
import com.example.solutionchallenge.datamodel.Exercise
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExerciseViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData: LiveData<List<Exercise>>
    val readBookmarkData: LiveData<List<Exercise>> //즐겨찾기 데이터 읽어오기
    private val repository: ExerciseRepository

    //이 부분 수정함******** 건드리지 말 것
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ExerciseViewModel::class.java)) {

                return ExerciseViewModel(application) as T
            }
            //  요청된 클래스가 ViewModel과 매치되지 않는 경우 예외를 발생
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    // get set
    private var _currentData = MutableLiveData<List<Exercise>>()
    val currentData: LiveData<List<Exercise>>
    get() = _currentData

    init {
        val exerciseDao =
            ExerciseDatabase.getDatabase(application)!!.exerciseDao()
        repository = ExerciseRepository(exerciseDao)
        readAllData = repository.readAllData.asLiveData()
        readBookmarkData = repository.readBookmarkData.asLiveData() //즐겨찾기 데이터 읽어오기
    }

    fun addExercise(exercise: Exercise){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addExercise(exercise)
        }
    }

    fun updateExercise(exercise: Exercise){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateExercise(exercise)
        }
    }

    fun deleteExercise(exercise: Exercise){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteExercise(exercise)
        }
    }

    }



