package com.myfirstandroidapp.helpcalendar

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
//import com.myfirstandroidapp.helpcalendar.databinding.ItemExerciseBinding
import com.example.solutionchallenge.databinding.ItemExerciseBinding
import com.myfirstandroidapp.helpcalendar.dialog.TimeDoneUpdateDialog
import com.myfirstandroidapp.helpcalendar.dialog.TimeGoalUpdateDialog
import com.myfirstandroidapp.helpcalendar.dialog.UpdateDialogInterface
import com.myfirstandroidapp.helpcalendar.model.Exercise


class ExerciseAdapter(private val exerciseViewModel: ExerciseViewModel) :
    RecyclerView.Adapter<ExerciseAdapter.MyViewHolder>() {

    private var exerciseList = emptyList<Exercise>()

    // 뷰 홀더에 데이터를 바인딩
    class MyViewHolder(private val binding: ItemExerciseBinding) :
        RecyclerView.ViewHolder(binding.root),
        UpdateDialogInterface {
        private lateinit var exercise: Exercise
        private lateinit var exerciseViewModel: ExerciseViewModel

        fun bind(currentExercise: Exercise, exerciseViewModel: ExerciseViewModel) {
            binding.exercise = currentExercise
            this.exerciseViewModel = exerciseViewModel

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
                exerciseViewModel.deleteExercise(currentExercise)
            }

            // 수정 버튼 클릭 시 다이얼로그 띄움
            binding.updateButton.setOnClickListener {
                exercise = currentExercise
                val TimeGoalUpdateDialog = TimeGoalUpdateDialog(binding.updateButton.context, this)
                TimeGoalUpdateDialog.show()
            }

            binding.progressBar.setOnClickListener {
                exercise = currentExercise
                val TimeDoneUpdateDialog = TimeDoneUpdateDialog(binding.progressBar.context, this)
                TimeDoneUpdateDialog.show()
            }
        }

        // 다이얼로그의 결과값으로 업데이트 해줌
        override fun onOkButtonClicked1(name: String, time: Int) {
            val updateExercise = Exercise(
                exercise.id,
                exercise.check,
                name,
                time,
                exercise.time_done,
                exercise.year,
                exercise.month,
                exercise.day
            )
            exerciseViewModel.updateExercise(updateExercise)
        }
        override fun onOkButtonClicked2(name: String, time: Int) {
            val updateExercise = Exercise(
                exercise.id,
                exercise.check,
                name,
                exercise.time_goal,
                time,
                exercise.year,
                exercise.month,
                exercise.day
            )
            exerciseViewModel.updateExercise(updateExercise)
        }
    }

    // 어떤 xml 으로 뷰 홀더를 생성할지 지정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemExerciseBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    // 바인딩 함수로 넘겨줌
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(exerciseList[position], exerciseViewModel)
    }

    // 뷰 홀더의 개수 리턴
    override fun getItemCount(): Int {
        return exerciseList.size
    }

    // 메모 리스트 갱신
    fun setData(exercise: List<Exercise>) {
        exerciseList = exercise
        notifyDataSetChanged()
    }

    // 아이템에 아이디를 설정해줌 (깜빡이는 현상방지)
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}