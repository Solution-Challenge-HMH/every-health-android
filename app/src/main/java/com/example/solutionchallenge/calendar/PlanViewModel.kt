package com.example.solutionchallenge.calendar


import android.app.Application
import androidx.lifecycle.*
import com.example.solutionchallenge.calendar.db.PlanDatabase
import com.example.solutionchallenge.calendar.model.Plan

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// 뷰모델은 DB에 직접 접근하지 않아야함. Repository 에서 데이터 통신.
class PlanViewModel(application: Application) : AndroidViewModel(application) {

    val readAllData : LiveData<List<Plan>>

    private val repository : PlanRepository

    //이 부분 수정함******** 건드리지 말 것
    class Factory(private val application: Application) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(PlanViewModel::class.java)) {
                // ExerciseViewModel 클래스에 매칭될 경우, 해당 클래스의 인스턴스를 생성하여 반환합니다.
                return PlanViewModel(application) as T
            }
            // 예외 처리: 요청된 클래스가 ExerciseViewModel과 매치되지 않는 경우 예외를 발생시킵니다.
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
    // get set
    private var _currentData = MutableLiveData<List<Plan>>()
    val currentData : LiveData<List<Plan>>
        get() = _currentData

    init{
        val planDao = PlanDatabase.getDatabase(application)!!.planDao()
        repository = PlanRepository(planDao)
        //readAllData = repository.readAllData.asLiveData()
        readAllData = repository.readAllData
    }

    fun addPlan(plan: Plan){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addPlan(plan)
        }
    }

    fun updatePlan(plan: Plan){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updatePlan(plan)
        }
    }

    fun deletePlan(plan: Plan){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deletePlan(plan)
        }
    }

    fun readDateData(year : Int, month : Int, day : Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val tmp = repository.readDateData(year, month, day)
            _currentData.postValue(tmp)
        }
    }


//    fun searchDatabase(searchQuery: String): LiveData<List<Memo>> {
//        return repository.searchDatabase(searchQuery).asLiveData()
//    }

}
