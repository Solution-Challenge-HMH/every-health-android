package com.example.solutionchallenge.calendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.solutionchallenge.calendar.db.PlanViewModel
import com.example.solutionchallenge.calendar.dialog.TimeGoalUpdateDialog
import com.example.solutionchallenge.calendar.dialog.UpdateDialogInterface
import com.example.solutionchallenge.calendar.model.Plan
import com.example.solutionchallenge.databinding.ItemPlanBinding
import com.example.solutionchallenge.calendar.dialog.TimeDoneUpdateDialog



class PlanAdapter(private val planViewModel: PlanViewModel) :
    RecyclerView.Adapter<PlanAdapter.MyViewHolder>() {

    private var planList = emptyList<Plan>()

    // 뷰 홀더에 데이터를 바인딩
    class MyViewHolder(private val binding: ItemPlanBinding) :
        RecyclerView.ViewHolder(binding.root),
        UpdateDialogInterface {
        private lateinit var plan: Plan
        private lateinit var planViewModel: PlanViewModel

        fun bind(currentPlan: Plan, planViewModel: PlanViewModel) {
            binding.plan = currentPlan
            this.planViewModel = planViewModel

            // 체크 리스너 초기화 해줘 중복 오류 방지
            binding.exerciseCheckBox.setOnCheckedChangeListener(null)

            /*
            // 메모 체크 시 체크 데이터 업데이트
            binding.exerciseCheckBox.setOnCheckedChangeListener { _, check ->
                if (check) {
                    exercise = Exercise( 0,true, currentExercise.name, currentExercise.time,
                        currentExercise.year, currentExercise.month, currentExercise.day)
                    this.exerciseViewModel.updateExercise(exercise)
                }
                else {
                    exercise = Exercise( 0,false, currentExercise.name,currentExercise.time,
                        currentExercise.year, currentExercise.month, currentExercise.day)
                    this.exerciseViewModel.updateExercise(exercise)
                }
            }
*/
            // 삭제 버튼 클릭 시 메모 삭제
            binding.deleteButton.setOnClickListener {
                planViewModel.deletePlan(currentPlan)
            }

            // 수정 버튼 클릭 시 다이얼로그 띄움
            binding.updateButton.setOnClickListener {
                plan = currentPlan
                val timeGoalUpdateDialog = TimeGoalUpdateDialog(binding.updateButton.context, this)
                timeGoalUpdateDialog.show()
            }

            binding.progressBar.setOnClickListener {
                plan = currentPlan
                val timeDoneUpdateDialog =TimeDoneUpdateDialog(binding.progressBar.context, this, selected_date="abc")
                timeDoneUpdateDialog.show()
            }
        }

        // 다이얼로그의 결과값으로 업데이트 해줌
        override fun onOkButtonClicked1(exerciseName: String,  plannedTime: Int, thisDate: String) { //캘린더 화면에서 날짜 고정된 상태로 운동 추가


            val updatePlan = Plan(
                plan.planId,
                plan.check,
                exerciseId=0, //name 이랑 연결
                exerciseName,
                plannedTime,
                plan.doneTime,
                plan.thisDate

            )
            planViewModel.updatePlan(updatePlan)
        }

        override fun onOkButtonClicked2(exerciseName: String, doneTime: Int,date: String) { //이미 추가된 운동의 "달성시간" 수정 (프로그레스바 터치)
            val updatePlan = Plan(
                plan.planId,
                plan.check,
                exerciseId=0, //name 이랑 연결
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
        holder.bind(planList[position], planViewModel)
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
}