package com.example.solutionchallenge.calendar

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.solutionchallenge.ServiceCreator
import com.example.solutionchallenge.activity.CalendarActivity

import com.example.solutionchallenge.calendar.dialog.UpdateDialogInterface
import com.example.solutionchallenge.databinding.ItemPlanBinding
import com.example.solutionchallenge.calendar.dialog.TimeDoneUpdateDialog
import com.example.solutionchallenge.calendar.model.Plan
import com.example.solutionchallenge.datamodel.DatePlan
import com.example.solutionchallenge.datamodel.ResponsePlanIdDELETEData
import com.example.solutionchallenge.datamodel.ResponsePlanThisDateData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PlanAdapter(
    private val planViewModel: PlanViewModel,
    private val accessToken: String,
) :
    RecyclerView.Adapter<PlanAdapter.MyViewHolder>() {

    val receivedAccessToken = accessToken

    private var planList = emptyList<Plan>()

    // 뷰 홀더에 데이터를 바인딩


    class MyViewHolder(private val binding: ItemPlanBinding) :
        RecyclerView.ViewHolder(binding.root),
        UpdateDialogInterface {
        private lateinit var plan: Plan
        private lateinit var planViewModel: PlanViewModel
        private val context: Context = binding.root.context



        @SuppressLint("SuspiciousIndentation")
        fun bind(currentPlan: Plan, planViewModel: PlanViewModel, receivedAccessToken: String) {
            binding.plan = currentPlan
            this.planViewModel = planViewModel
            val planId = currentPlan.planId
            val thisDate = currentPlan.thisDate
            // 체크 리스너 초기화 중복 오류 방지
            binding.exerciseCheckBox.setOnCheckedChangeListener(null)

            // 삭제 버튼 클릭 시 플랜 삭제
            binding.deleteButton.setOnClickListener {
                val receivedAccessToken = receivedAccessToken


                // 플랜 삭제 API 호출
                val callPlanDelete: Call<ResponsePlanIdDELETEData> =
                    ServiceCreator.everyHealthService.deletePlanPlanId(
                        "Bearer $receivedAccessToken",
                        planId
                    )
                Log.d(TAG, "$receivedAccessToken")

                callPlanDelete.enqueue(object : Callback<ResponsePlanIdDELETEData> {
                    override fun onResponse(
                        call: Call<ResponsePlanIdDELETEData>,
                        response: Response<ResponsePlanIdDELETEData>
                    ) {
                        Log.d(TAG, "Sending DELETE request to remove planId: $planId")

                        if (response.isSuccessful) {
                            Log.d(TAG, "플랜 삭제 성공")
                            planViewModel.deletePlan(currentPlan)

                        } else {
                            Log.d(TAG, "플랜 삭제 실패")
                        }
                    }

                    override fun onFailure(
                        call: Call<ResponsePlanIdDELETEData>,
                        t: Throwable
                    ) {
                        Log.e("NetworkTest", "error:$t")
                    }
                })

                val intent = Intent(context, CalendarActivity::class.java)
                intent.putExtra("receivedAccessToken", receivedAccessToken)
                context.startActivity(intent)
            }

            binding.progressBar.setOnClickListener {
                plan = currentPlan
                val timeDoneUpdateDialog =
                    TimeDoneUpdateDialog(binding.progressBar.context, this, selected_date = "abc")
                timeDoneUpdateDialog.show()
            }
        }

        // 다이얼로그의 결과값으로 업데이트 해줌
        override fun onOkButtonClicked1(
            exerciseId: Int,
            exerciseName: String,
            plannedTime: Int,
            thisDate: String
        ) { //캘린더 화면에서 날짜 고정된 상태로 운동 추가

        }

        override fun onOkButtonClicked2(
            exerciseName: String,
            doneTime: Int,
            date: String
        ) { //이미 추가된 운동의 "달성시간" 수정 (프로그레스바 터치)
            val updatePlan = Plan(
                plan.planId,
                plan.check,
                exerciseId = 0, //name 이랑 연결
                exerciseName,
                plan.plannedTime,
                doneTime,
                plan.thisDate
            )
            planViewModel.updatePlan(updatePlan)
        }

    }

    // 어떤 xml 으로 뷰 홀더를 생성할지 지정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    // 바인딩 함수로 넘겨줌
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(planList[position], planViewModel, receivedAccessToken)
    }

    // 뷰 홀더의 개수 리턴
    override fun getItemCount(): Int {
        return planList.size
    }

    // 메모 리스트 갱신
    fun setData(plan: List<Plan>) {
        planList = plan
        notifyDataSetChanged()
    }

    // 아이템에 아이디를 설정해줌 (깜빡이는 현상방지)
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }


    companion object {
        private const val TAG = "PlanAdapter"
    }


}

