package com.myfirstandroidapp.helpcalendar

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
//import com.myfirstandroidapp.helpcalendar.databinding.FragmentCalendarBinding
import com.example.solutionchallenge.databinding.FragmentCalendarBinding
import com.myfirstandroidapp.helpcalendar.dialog.CustomDialog
import com.myfirstandroidapp.helpcalendar.dialog.CustomDialogInterface
import com.myfirstandroidapp.helpcalendar.dialog.UpdateDialogInterface
import com.myfirstandroidapp.helpcalendar.model.Exercise

class CalendarFragment : Fragment(), CustomDialogInterface, UpdateDialogInterface {

    private var binding: FragmentCalendarBinding? = null
    private val exerciseViewModel: ExerciseViewModel by viewModels {
        ExerciseViewModel.Factory(requireActivity().application)
    } // 뷰모델 연결 (수정함******************)
    private val adapter: ExerciseAdapter by lazy { ExerciseAdapter(exerciseViewModel) } // 어댑터 선언

    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 뷰바인딩
        binding = FragmentCalendarBinding.inflate(inflater, container, false)

        // 아이템에 아이디를 설정해줌 (깜빡이는 현상방지)
        adapter.setHasStableIds(true)

        // 아이템을 가로로 하나씩 보여주고 어댑터 연결
        binding!!.calendarRecyclerview.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding!!.calendarRecyclerview.adapter = adapter

        /*
                binding!!.calendarView.setOnDateChangeListener { _, year, month, day ->
                    // 날짜 선택시 그 날의 정보 할당
                    this.year = year
                    this.month = month + 1
                    this.day = day


         */

        // 달력 - 날짜 선택 Listener
        binding!!.calendarView.setOnDateChangedListener { _, date, _ ->
            year = date.year
            month = date.month + 1
            day = date.day

            binding!!.calendarDateText.text = "${this.year}/${this.month}/${this.day}"

            // 해당 날짜 데이터를 불러옴 (currentData 변경)
            exerciseViewModel.readDateData(this.year, this.month, this.day)
        }

        // 메모 데이터가 수정되었을 경우 날짜 데이터를 불러옴 (currentData 변경)
        exerciseViewModel.readAllData.observe(viewLifecycleOwner) {
            exerciseViewModel.readDateData(year, month, day)
        }

        // 현재 날짜 데이터 리스트(currentData) 관찰하여 변경시 어댑터에 전달해줌
        exerciseViewModel.currentData.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })

        // Fab 클릭시 다이얼로그 띄움
        binding!!.calendarDialogButton.setOnClickListener {
            if (year == 0) {
                Toast.makeText(activity, "날짜를 선택해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                onFabClicked()
            }
        }



        return binding!!.root
    }

    // Fab 클릭시 사용되는 함수
    private fun onFabClicked() {
        val CustomDialog = CustomDialog(requireActivity(), this)
        CustomDialog.show()
    }

    // 프래그먼트는 뷰보다 오래 지속 . 프래그먼트의 onDestroyView() 메서드에서 결합 클래스 인스턴스 참조를 정리
    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onOkButtonClicked1(name: String, time_goal: Int) {
        // 선택된 날짜로 메모를 추가해줌
        val exercise = Exercise(0, false, name, time_goal, time_done = 0, year, month, day)
        exerciseViewModel.addExercise(exercise)
        Toast.makeText(activity, "추가", Toast.LENGTH_SHORT).show()
    }


    override fun onOkButtonClicked2(name: String, time: Int) {
        TODO("Not yet implemented")
    }
    /*
        override fun onOkButtonClicked(name: String, time: Int) {
            TODO("Not yet implemented")
        }

     */

}