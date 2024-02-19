package com.example.solutionchallenge.calendar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.solutionchallenge.PlanAddDialog
import com.example.solutionchallenge.RecommendationDetailDialog
import com.example.solutionchallenge.ServiceCreator
import com.example.solutionchallenge.adapter.ExerciseAdapter
import com.example.solutionchallenge.calendar.PlanViewModel
import com.example.solutionchallenge.calendar.dialog.CustomDialog
import com.example.solutionchallenge.calendar.dialog.CustomDialogInterface
import com.example.solutionchallenge.calendar.dialog.UpdateDialogInterface
import com.example.solutionchallenge.calendar.model.Plan
//import com.myfirstandroidapp.helpcalendar.databinding.FragmentCalendarBinding
import com.example.solutionchallenge.databinding.FragmentCalendarBinding
import com.example.solutionchallenge.datamodel.Exercise
import com.example.solutionchallenge.datamodel.RequestPlanData
import com.example.solutionchallenge.datamodel.ResponseExerciseData
import com.example.solutionchallenge.datamodel.ResponseExerciseExerciseIdData
import com.example.solutionchallenge.datamodel.ResponsePlanCalendarData
import com.example.solutionchallenge.datamodel.ResponsePlanData
import com.example.solutionchallenge.datamodel.ResponsePlanThisDateData
import com.example.solutionchallenge.datamodel.ResponsePlanTodayData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat


class CalendarFragment : Fragment(), CustomDialogInterface, UpdateDialogInterface {

    private var binding: FragmentCalendarBinding? = null
    private val planViewModel: PlanViewModel by viewModels {
        PlanViewModel.Factory(requireActivity().application)
    } // 뷰모델 연결 (수정함******************)
    private val adapter: PlanAdapter by lazy { PlanAdapter(planViewModel) } // 어댑터 선언

    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    private var thisDate: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 뷰바인딩
        binding = FragmentCalendarBinding.inflate(inflater, container, false)

        // 아이템에 아이디를 설정해줌 (깜빡이는 현상방지)
        adapter.setHasStableIds(true)


        //전체 달력 플랜 가져오기 - getPlanCalendar()
        val receivedAccessToken = arguments?.getString("receivedAccessToken")
        val callGetCalendar: Call<ResponsePlanCalendarData> =
            ServiceCreator.everyHealthService.getPlanCalendar("Bearer $receivedAccessToken")

        callGetCalendar.enqueue(object :Callback<ResponsePlanCalendarData>{
            override fun onResponse(
                call: Call<ResponsePlanCalendarData>,
                response: Response<ResponsePlanCalendarData>
            ) {
                if (response.isSuccessful) {
                    Log.d(TAG, " 달력 전체 플랜 가져오기 성공")
                    val responsePlanCalendarData = response.body()
                    if(responsePlanCalendarData != null){
                        val planCalendarData = responsePlanCalendarData.data
                        Log.d("planCalendarData", "$planCalendarData")
                    }
                } else {
                    Log.d(TAG, "달력 전체 플랜 가져오기 실패")
                }
            }

            override fun onFailure(call: Call<ResponsePlanCalendarData>, t: Throwable) {
                Log.e("NetworkTest", "error:$t")
            }
        })

        // 아이템을 가로로 하나씩 보여주고 어댑터 연결
        binding!!.calendarRecyclerview.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding!!.calendarRecyclerview.adapter = adapter


        // 달력 - 날짜 선택 Listener
        binding!!.calendarView.setOnDateChangeListener { _, year, month, day->

            this.year = year
            this.month = month + 1
            this.day = day

            binding!!.calendarDateText.text = "${this.year}-${String.format("%02d", this.month)}-${String.format("%02d", this.day)}"
            thisDate = "${this.year}-${String.format("%02d", this.month)}-${String.format("%02d", this.day)}"


            //지정날짜에 대한 PlanList 불러오기
            val callPlanOfThisDate: Call<ResponsePlanThisDateData> =
                ServiceCreator.everyHealthService.getPlanOfThisDate("Bearer $receivedAccessToken",thisDate)


            callPlanOfThisDate.enqueue(object : Callback<ResponsePlanThisDateData> {
                override fun onResponse(
                    call: Call<ResponsePlanThisDateData>,
                    response: Response<ResponsePlanThisDateData>
                ) {
                    if (response.isSuccessful) {
                        val responsePlanOfThisDateData = response.body()
                        if (responsePlanOfThisDateData != null) {
                            val thisDatePlanDetail = responsePlanOfThisDateData.data
                         Log.d("thisDatePlanDetail", "$thisDatePlanDetail")
                        } else {
                            Log.d(TAG, "지정 날짜 플랜 가져오기 성공")
                        }
                    } else {
                        Log.d(TAG, "지정 날짜 플랜 가져오기 성공")
                    }
                }

                override fun onFailure(call: Call<ResponsePlanThisDateData>, t: Throwable) {
                    Log.e(TAG, "지정 날짜 플랜 가져오기 요청 실패: $t")
                }
            })
            // 해당 날짜 데이터를 불러옴 (currentData 변경)
            planViewModel.readDateData(year, month+1, day)
        }

        // 메모 데이터가 수정되었을 경우 날짜 데이터를 불러옴 (currentData 변경)
        planViewModel.readAllData.observe(viewLifecycleOwner) {
            // 현재 날짜 데이터 리스트(currentData) 관찰하여 변경시 어댑터에 전달해줌
            planViewModel.readDateData(this.year, this.month + 1, this.day)
        }

        // 현재 날짜 데이터 리스트(currentData) 관찰하여 변경시 어댑터에 전달해줌
        planViewModel.currentData.observe(viewLifecycleOwner, Observer {
            adapter.setData(it)
        })
        // Fab 클릭시 다이얼로그 띄움
        binding!!.calendarDialogButton.setOnClickListener {
            if (year == 0) {
                Toast.makeText(activity, "날짜를 선택해주세요.", Toast.LENGTH_SHORT).show()
            } else {

                //데이터 포맷 수정
                val selectedDate = "${this.year}-${String.format("%02d", this.month)}-${String.format("%02d", this.day)}"
                onFabClicked(selectedDate)
            }
        }

        return binding!!.root
    }

    // Fab 클릭시 사용되는 함수
    private fun onFabClicked(selectedDate: String) {
        val receivedAccessToken = arguments?.getString("receivedAccessToken")

        val callExercise: Call<ResponseExerciseData> =
            ServiceCreator.everyHealthService.getExercise("Bearer $receivedAccessToken")

        callExercise.enqueue(object : Callback<ResponseExerciseData> {
            override fun onResponse(call: Call<ResponseExerciseData>, response: Response<ResponseExerciseData>) {
                if (response.isSuccessful) {
                    val responseExerciseData = response.body()
                    if (responseExerciseData != null) {

                        val exerciseList = responseExerciseData.data
                        val parcelableExerciseList = ArrayList<Exercise>(exerciseList.size)

                        exerciseList.forEach { exercise ->
                            val parcelableExercise = Exercise(exercise.id, exercise.name, exercise.time, exercise.difficulty,
                                exercise.description, exercise.caution, exercise.reference, exercise.bookmarked)
                            parcelableExerciseList.add(parcelableExercise)
                        }


                        // 운동 목록을 다이얼로그로 전달하여 표시
                        val customDialog = CustomDialog(requireActivity(), this@CalendarFragment, selectedDate, parcelableExerciseList)
                        customDialog.show()
                    }
                } else {
                    // 서버 요청이 실패한 경우 처리
                    Toast.makeText(requireContext(), "운동목록을 가져오는데 실패하였습니다.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseExerciseData>, t: Throwable) {
                // 네트워크 오류 등으로 서버 요청이 실패한 경우 처리
                Toast.makeText(requireContext(), "네트워크 오류로 운동목록을 가져오는데 실패하였습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }


    // 프래그먼트는 뷰보다 오래 지속 . 프래그먼트의 onDestroyView() 메서드에서 결합 클래스 인스턴스 참조를 정리
    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onOkButtonClicked1(
        exerciseId: Int,
        exerciseName: String,
        plannedTime: Int,
        thisDate: String
    ) {

        val receivedAccessToken = arguments?.getString("receivedAccessToken")
        // plannedDate가 null이거나 빈 문자열인 경우 예외 처리
        if (thisDate.isNotBlank()) {
            // 선택된 날짜로 메모를 추가해줌
            val plan = Plan(0, false, exerciseId, exerciseName, plannedTime, doneTime = 0, thisDate)
            planViewModel.addPlan(plan)
            Toast.makeText(activity, "추가됨", Toast.LENGTH_SHORT).show()


          //  val outputDateString = convertDateFormat(thisDate, "yyyy-M-d", "yyyy-MM-dd")
          //  Log.d("datefix", outputDateString)
            val requestPlanData = RequestPlanData(
                exerciseId,
                thisDate,
                plannedTime)
            Log.d(TAG, "RequestPlanData: $requestPlanData") // 서버로 보내기 전 확인용

            val call: Call<ResponsePlanData> =
                ServiceCreator.everyHealthService.postPlan("Bearer $receivedAccessToken",requestPlanData)

            call.enqueue(object : Callback<ResponsePlanData> {
                override fun onResponse(
                    call: Call<ResponsePlanData>,
                    response: Response<ResponsePlanData>
                ) {
                    if (response.isSuccessful) {
                        Log.d(TAG, "플랜 전송 성공")
                    } else {
                        Log.d(TAG, "플랜 전송 실패")
                    }
                }
                override fun onFailure(call: Call<ResponsePlanData>, t: Throwable) {
                    Log.e("NetworkTest", "error:$t")
                }
            })
        } else {
            // 예외 처리: plannedDate가 null이거나 빈 문자열인 경우
            Toast.makeText(activity, "날짜를 선택해주세요.", Toast.LENGTH_SHORT).show()
        }
    }

    /*
    private fun convertDateFormat(inputDateString: String, inputFormatString: String, outputFormatString: String): String {
        val inputFormat = SimpleDateFormat(inputFormatString)
        val outputFormat = SimpleDateFormat(outputFormatString)

        val inputDate = inputFormat.parse(inputDateString) // 입력된 문자열을 날짜로 파싱
        return outputFormat.format(inputDate) // 날짜를 지정된 형식의 문자열로 변환하여 반환
    }
*/
    override fun onOkButtonClicked2(
        //exerciseId: Int,
        exerciseName: String,
        doneTime: Int,
        thisDate: String
    ) {
        TODO("Not yet implemented")
    }



    companion object {
        const val TAG = "CalendarFragment"
        fun newInstance(receivedAccessToken: String?): CalendarFragment {
            val fragment = CalendarFragment()
            val args = Bundle()
            args.putString("receivedAccessToken", receivedAccessToken)
            fragment.arguments = args
            return fragment
        }
    }

}