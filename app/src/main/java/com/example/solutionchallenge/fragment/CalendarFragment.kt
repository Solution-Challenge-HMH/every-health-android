package com.example.solutionchallenge.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.solutionchallenge.decorator.OneDayDecorator
import com.example.solutionchallenge.decorator.PlanDayDecorator
import com.example.solutionchallenge.R
import com.example.solutionchallenge.serverdata.ServiceCreator
import com.example.solutionchallenge.activity.CalendarActivity
import com.example.solutionchallenge.activity.LogOutActivity
import com.example.solutionchallenge.activity.MainActivity
import com.example.solutionchallenge.activity.StopWatchActivity
import com.example.solutionchallenge.adapter.PlanAdapter
import com.example.solutionchallenge.viewmodel.PlanViewModel
import com.example.solutionchallenge.dialog.CustomDialog
import com.example.solutionchallenge.dialog.CustomDialogInterface
import com.example.solutionchallenge.datamodel.Plan
//import com.myfirstandroidapp.helpcalendar.databinding.FragmentCalendarBinding
import com.example.solutionchallenge.databinding.FragmentCalendarBinding
import com.example.solutionchallenge.datamodel.Exercise
import com.example.solutionchallenge.serverdata.RequestPlanData
import com.example.solutionchallenge.serverdata.ResponseExerciseData
import com.example.solutionchallenge.serverdata.ResponsePlanCalendarData
import com.example.solutionchallenge.serverdata.ResponsePlanData
import com.example.solutionchallenge.serverdata.ResponsePlanThisDateData
import com.prolificinteractive.materialcalendarview.CalendarDay
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
//전체 달력 플랜 가져오기 - getPlanCalendar()
        val receivedAccessToken = arguments?.getString("receivedAccessToken").toString()
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

                        val emptyPlanDates = planCalendarData.filter { it.planList.isEmpty() }.map { it.date }
                        val nonEmptyPlanDates = planCalendarData.filter { it.planList.isNotEmpty() }.map { it.date }
                        Log.d("noplandate", "$emptyPlanDates")
                        Log.d("yesplandate", "$nonEmptyPlanDates")

                        val decorator = PlanDayDecorator(requireContext(), nonEmptyPlanDates)

                        // MaterialCalendarView에 데코레이터 추가
                        binding!!.calendarView.addDecorator(decorator)
                    }
                } else {
                    Log.d(TAG, "달력 전체 플랜 가져오기 실패")
                }
            }

            override fun onFailure(call: Call<ResponsePlanCalendarData>, t: Throwable) {
                Log.e("NetworkTest", "error:$t")
            }
        })
//여기부터
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
//여기까지 오늘날짜 선택되게??

        // 아이템을 가로로 하나씩 보여주고 어댑터 연결
        binding!!.calendarRecyclerview.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        binding!!.calendarRecyclerview.adapter = adapter

        //캘린더 데코!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        binding!!.calendarView.selectedDate = CalendarDay.today()

        val decorator = OneDayDecorator( this)
        binding!!.calendarView.addDecorator(decorator)


        // 달력 - 날짜 선택 Listener
        binding!!.calendarView.setOnDateChangedListener { _, date, _ ->
            val year = date.year
            val month = date.month + 1
            val day = date.day

            Log.d("mater", "$year, $month, $day")

            this.year = year
            this.month = month
            this.day = day
            Log.d("mater", "$this.year, $this.month, $this.day")

            binding!!.calendarDateText.text = "${this.year}-${String.format("%02d", this.month)}-${String.format("%02d", this.day)}"
            thisDate = "${this.year}-${String.format("%02d", this.month)}-${String.format("%02d", this.day)}"



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
        planViewModel.currentData.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }


        // Fab 클릭시 다이얼로그 띄움
        binding!!.calendarDialogButton.setOnClickListener {
            if (year == 0) {
                Toast.makeText(activity, "Please choose a date.", Toast.LENGTH_SHORT).show()
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

        binding!!.stopwatchImage.setOnClickListener{
            val intent = Intent(context, StopWatchActivity::class.java)
            intent.putExtra("receivedAccessToken", receivedAccessToken)
            startActivity(intent)

        }
        toolbarButton(binding!!.toolbar, receivedAccessToken)

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
                    Toast.makeText(requireContext(), "Failed to get exercise list.", Toast.LENGTH_SHORT) //운동목록을 가져오는데 실패하였습니다.
                        .show()
                }
            }

            override fun onFailure(call: Call<ResponseExerciseData>, t: Throwable) {
                // 네트워크 오류 등으로 서버 요청이 실패한 경우 처리
                Toast.makeText(
                    requireContext(),
                    "Failed to get exercise list.",
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
                            Toast.makeText(activity, "Successfully added to your calendar.", Toast.LENGTH_SHORT).show()
                            planViewModel.addPlan(plan)


                            //지정날짜에 대한 PlanList 불러오기 -> 일정 추가 한 후에 실행해서 ui에 보이게 하기
                            updateRecycler()




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
            Toast.makeText(activity, "Please choose a date.", Toast.LENGTH_SHORT).show()
        }
    }

fun updateRecycler(){ //지정날짜에 대한 PlanList 불러오기 -> 추가/삭제/수정시 실행해서 리사이클럽뷰 업데이트
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
                    Log.d(TAG, "지정 날짜 플랜 가져오기 성공")
                    val thisDatePlanDetail = responsePlanOfThisDateData.data
                    Log.d("thisDatePlanDetail", "$thisDatePlanDetail")
                    val allPlans = thisDatePlanDetail.planList
                    adapter.setData(allPlans)
                    for (plan in allPlans) {
                        planViewModel.addPlan(plan)
                    }
                } else {
                    Log.d(TAG, "responsePlanOfThisDateData == null")
                }
            } else {
                Log.d(TAG, "지정 날짜 플랜 가져오기 실패")
            }
        }

        override fun onFailure(call: Call<ResponsePlanThisDateData>, t: Throwable) {
            Log.e(TAG, "지정 날짜 플랜 가져오기 요청 실패: $t")
        }
    })

}
    fun toolbarButton(toolbar: androidx.appcompat.widget.Toolbar, receivedAccessToken: String?){

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.ToCalendarButtonInMenu -> { // 메뉴 아이템의 ID에 따라 동작을 결정합니다.
                    // 다음 activity로 이동하는 코드를 작성합니다.
                    /*
                                        Log.i("CalFrag", "캘린더 아이콘 클릭됨")
                                        val intent = Intent(context, CalendarActivity::class.java)
                                        startActivity(intent)
                                        true
                    */
                    val intent = Intent(context, CalendarActivity::class.java)
                    intent.putExtra("receivedAccessToken", receivedAccessToken)
                    startActivity(intent)
                    //finish()
                    true// 현재 액티비티 종료
                }
                R.id.ToMypageButtonInMenu -> { // 메뉴 아이템의 ID에 따라 동작을 결정합니다.
                    // 다음 activity로 이동하는 코드를 작성합니다.
                    Log.i("CalFrag", "마이페이지 아이콘 클릭됨")
                    val intent = Intent(context, LogOutActivity::class.java)
                    intent.putExtra("receivedAccessToken", receivedAccessToken)
                    startActivity(intent)
                    true
                }
                R.id.ToMainButtonInMenu -> { // 메뉴 아이템의 ID에 따라 동작을 결정합니다.
                    // 다음 activity로 이동하는 코드를 작성합니다.
                    Log.i("CalFrag", "로고 클릭")
                    val intent = Intent(context, MainActivity::class.java)
                    intent.putExtra("receivedAccessToken", receivedAccessToken)
                    startActivity(intent)
                    true
                }
                else -> false
            }
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