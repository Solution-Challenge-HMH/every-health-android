package com.example.solutionchallenge.calendar

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.solutionchallenge.ServiceCreator
import com.example.solutionchallenge.calendar.dialog.CustomDialog
import com.example.solutionchallenge.calendar.dialog.CustomDialogInterface
import com.example.solutionchallenge.calendar.dialog.UpdateDialogInterface
import com.example.solutionchallenge.calendar.model.Plan
//import com.myfirstandroidapp.helpcalendar.databinding.FragmentCalendarBinding
import com.example.solutionchallenge.databinding.FragmentCalendarBinding
import com.example.solutionchallenge.datamodel.Exercise
import com.example.solutionchallenge.datamodel.RequestPlanData
import com.example.solutionchallenge.datamodel.ResponseExerciseData
import com.example.solutionchallenge.datamodel.ResponsePlanCalendarData
import com.example.solutionchallenge.datamodel.ResponsePlanData
import com.example.solutionchallenge.datamodel.ResponsePlanThisDateData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class CalendarFragment : Fragment(), CustomDialogInterface {

    private lateinit var receivedAccessToken: String

    private var binding: FragmentCalendarBinding? = null
    private val planViewModel: PlanViewModel by viewModels {
        PlanViewModel.Factory(requireActivity().application)
    } // 뷰모델 연결 (수정함******************)

    private var year: Int = 0
    private var month: Int = 0
    private var day: Int = 0
    private var thisDate: String = ""

    val adapter: PlanAdapter by lazy { PlanAdapter(planViewModel, receivedAccessToken) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        receivedAccessToken = arguments?.getString("receivedAccessToken").toString()

        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        adapter.setHasStableIds(true)


        val calendar = Calendar.getInstance()
        val todayDate = calendar.time

        // 날짜를 특정 형식으로 포맷팅
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(todayDate)

        year = formattedDate?.split("-")?.get(0)?.toInt()!!
        month = formattedDate?.split("-")?.get(1)?.toInt()!!
        day = formattedDate?.split("-")?.get(2)?.toInt()!!


        // 텍스트뷰에 오늘 날짜 설정
        binding?.calendarDateText?.text = formattedDate

        val callPlanToday: Call<ResponsePlanThisDateData> =
            ServiceCreator.everyHealthService.getPlanOfThisDate(
                "Bearer $receivedAccessToken",
                formattedDate
            )


        callPlanToday.enqueue(object : Callback<ResponsePlanThisDateData> {
            override fun onResponse(
                call: Call<ResponsePlanThisDateData>,
                response: Response<ResponsePlanThisDateData>
            ) {
                if (response.isSuccessful) {
                    val responsePlanOfToday = response.body()
                    if (responsePlanOfToday != null) {
                        val thisDatePlanDetail = responsePlanOfToday.data
                        Log.d("thisDatePlanDetail", "$thisDatePlanDetail")
                        val allPlans = thisDatePlanDetail.planList
                        // planViewModel.readDateData(year, month, day)
                        adapter.setData(allPlans)
                        //  for (plan in allPlans) {
                        //    planViewModel.addPlan(plan)
                        // }
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

        // 아이템을 가로로 하나씩 보여주고 어댑터 연결
        binding!!.calendarRecyclerview.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding!!.calendarRecyclerview.adapter = adapter


        // 달력 - 날짜 선택 Listener
        binding!!.calendarView.setOnDateChangeListener { _, year, month, day ->

            this.year = year
            this.month = month + 1
            this.day = day

            binding!!.calendarDateText.text = "${this.year}-${String.format("%02d", this.month)}-${
                String.format(
                    "%02d",
                    this.day
                )
            }"
            thisDate = "${this.year}-${String.format("%02d", this.month)}-${
                String.format(
                    "%02d",
                    this.day
                )
            }"


            //지정날짜에 대한 PlanList 불러오기
            val callPlanOfThisDate: Call<ResponsePlanThisDateData> =
                ServiceCreator.everyHealthService.getPlanOfThisDate(
                    "Bearer $receivedAccessToken",
                    thisDate
                )


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
                            val allPlans = thisDatePlanDetail.planList
                            adapter.setData(allPlans)
                            for (plan in allPlans) {
                                planViewModel.addPlan(plan)
                            }
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

        }
        planViewModel.readDateData(this.year, this.month, this.day)




        // currentData LiveData를 관찰하여 변경이 감지될 때마다 RecyclerView 어댑터 업데이트
        // ******************** 얜 뭐하는 애지????? **********************
        planViewModel.currentData.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }


        // Fab 클릭시 다이얼로그 띄움
        binding!!.calendarDialogButton.setOnClickListener {
            if (year == 0) {
                Toast.makeText(activity, "날짜를 선택해주세요.", Toast.LENGTH_SHORT).show()
            } else {

                //데이터 포맷 수정
                val selectedDate = "${this.year}-${String.format("%02d", this.month)}-${
                    String.format(
                        "%02d",
                        this.day
                    )
                }"
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
            override fun onResponse(
                call: Call<ResponseExerciseData>,
                response: Response<ResponseExerciseData>
            ) {
                if (response.isSuccessful) {
                    val responseExerciseData = response.body()
                    if (responseExerciseData != null) {

                        val exerciseList = responseExerciseData.data
                        val parcelableExerciseList = ArrayList<Exercise>(exerciseList.size)

                        exerciseList.forEach { exercise ->
                            val parcelableExercise = Exercise(
                                exercise.id,
                                exercise.name,
                                exercise.time,
                                exercise.difficulty,
                                exercise.description,
                                exercise.caution,
                                exercise.reference,
                                exercise.bookmarked
                            )
                            parcelableExerciseList.add(parcelableExercise)
                        }


                        // 운동 목록을 다이얼로그로 전달하여 표시
                        val customDialog = CustomDialog(
                            requireActivity(),
                            this@CalendarFragment,
                            selectedDate,
                            parcelableExerciseList
                        )
                        customDialog.show()
                    }
                } else {
                    // 서버 요청이 실패한 경우 처리
                    Toast.makeText(requireContext(), "운동목록을 가져오는데 실패하였습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onFailure(call: Call<ResponseExerciseData>, t: Throwable) {
                // 네트워크 오류 등으로 서버 요청이 실패한 경우 처리
                Toast.makeText(
                    requireContext(),
                    "네트워크 오류로 운동목록을 가져오는데 실패하였습니다.",
                    Toast.LENGTH_SHORT
                ).show()
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
            val requestPlanData = RequestPlanData(
                exerciseId,
                thisDate,
                plannedTime
            )
            Log.d(TAG, "RequestPlanData: $requestPlanData") // 서버로 보내기 전 확인용

            val call: Call<ResponsePlanData> =
                ServiceCreator.everyHealthService.postPlan(
                    "Bearer $receivedAccessToken",
                    requestPlanData
                )

            call.enqueue(object : Callback<ResponsePlanData> {
                override fun onResponse(
                    call: Call<ResponsePlanData>,
                    response: Response<ResponsePlanData>
                ) {
                    if (response.isSuccessful) {
                        Log.d(TAG, "플랜 전송 성공")

                        val responseData = response.body()

                        if (responseData != null) {

                            Log.d(TAG, "status: ${responseData.status}")
                            Log.d(TAG, "message: ${responseData.message}")
                            Log.d(TAG, "planId: ${responseData.data.planId}")
                            Log.d(TAG, "exerciseId: ${responseData.data.exerciseId}")
                            Log.d(TAG, "exerciseName: ${responseData.data.exerciseName}")
                            Log.d(TAG, "plannedTime: ${responseData.data.plannedTime}")
                            Log.d(TAG, "doneTime: ${responseData.data.doneTime}")

                            val plan = Plan(
                                responseData.data.planId,
                                check = false,
                                responseData.data.exerciseId,
                                responseData.data.exerciseName,
                                responseData.data.plannedTime,
                                responseData.data.doneTime,
                                thisDate
                            )
                            Toast.makeText(activity, "추가됨", Toast.LENGTH_SHORT).show()
                            planViewModel.addPlan(plan)


                            //지정날짜에 대한 PlanList 불러오기 -> 일정 추가 한 후에 실행해서 ui에 보이게 하기
                            val callPlanOfThisDate: Call<ResponsePlanThisDateData> =
                                ServiceCreator.everyHealthService.getPlanOfThisDate(
                                    "Bearer $receivedAccessToken",
                                    thisDate
                                )


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
                                            val allPlans = thisDatePlanDetail.planList
                                            adapter.setData(allPlans)
                                            for (plan in allPlans) {
                                                planViewModel.addPlan(plan)
                                            }
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





                        } else {
                            Log.d("NetworkTest", "sth wrong")

                        }


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