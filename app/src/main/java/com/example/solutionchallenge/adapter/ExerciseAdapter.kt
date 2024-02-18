package com.example.solutionchallenge.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.solutionchallenge.ExerciseDiffCallback
import com.example.solutionchallenge.RecommendationDetailDialog
import com.example.solutionchallenge.ServiceCreator
import com.example.solutionchallenge.databinding.ItemRecommendationBinding
import com.example.solutionchallenge.databinding.RecommendationDetailDialogBinding
import com.example.solutionchallenge.datamodel.Exercise
import com.example.solutionchallenge.datamodel.ResponseExerciseExerciseIdData
import com.example.solutionchallenge.viewmodel.ExerciseViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ExerciseAdapter(private val exerciseViewModel: ExerciseViewModel, private val isBookmarkVisible: Boolean)
    : RecyclerView.Adapter<ExerciseAdapter.MyViewHolder>() {

    private var recommendationList = emptyList<Exercise>()
    private var receivedAccessToken: String? = null

    inner class MyViewHolder(private val binding: ItemRecommendationBinding) : RecyclerView.ViewHolder(binding.root) {

        lateinit var exercise: Exercise
        private lateinit var exerciseViewModel: ExerciseViewModel

        fun bind(currentExercise: Exercise, exerciseViewModel: ExerciseViewModel) {
            binding.exercise = currentExercise
            this.exerciseViewModel = exerciseViewModel

            //즐찾버튼 숨김/보임
            if (!isBookmarkVisible) {
                binding.BookmarkCheckButton.visibility = View.GONE
            } else {
                binding.BookmarkCheckButton.visibility = View.VISIBLE
            }


            binding.BookmarkCheckButton.setOnCheckedChangeListener(null)

// BookmarkCheckButton 관련 로직 수정필요
     /*       // 메모 체크 시 체크 데이터 업데이트 -- 에러 있음
            binding.BookmarkCheckButton.setOnCheckedChangeListener { _, check ->
                if (check) {
                    exercise = Exercise(
                        currentExercise.id,
                        currentExercise.name,
                        currentExercise.time,
                        currentExercise.difficulty,
                        currentExercise.description,
                        currentExercise.caution,
                        currentExercise.reference,
                        true
                    )
                    this.exerciseViewModel.addExercise(exercise)



                } else {
                    exercise = Exercise(
                        currentExercise.id,
                        currentExercise.name,
                        currentExercise.time,
                        currentExercise.difficulty,
                        currentExercise.description,
                        currentExercise.caution,
                        currentExercise.reference,
                        false
                    )
                    this.exerciseViewModel.deleteExercise(exercise)

                }

                Log.d("BookmarkStatus", "Bookmark status ${exercise.bookmarked} =? $check  for item ${currentExercise.name}")
            }
*/
            binding.ToRecommendationDetailDialogButton.setOnClickListener{
                // currentExercise의 id 값을 가져옴
                val exerciseId = currentExercise.id

                // 운동의 상세 정보를 가져오기 위해 서버로 요청
                val callExerciseDetail: Call<ResponseExerciseExerciseIdData> =
                    ServiceCreator.everyHealthService.getExerciseExerciseId("Bearer $receivedAccessToken", exerciseId)

                callExerciseDetail.enqueue(object : Callback<ResponseExerciseExerciseIdData> {
                    override fun onResponse(
                        call: Call<ResponseExerciseExerciseIdData>,
                        response: Response<ResponseExerciseExerciseIdData>
                    ) {
                        if (response.isSuccessful) {
                            val responseExerciseDetailData = response.body()
                            if (responseExerciseDetailData != null) {
                                val exerciseDetail = responseExerciseDetailData.data
                                // 운동의 상세 정보를 사용하여 다이얼로그를 띄우는 등의 작업 수행
                                val recommendationDialog = RecommendationDetailDialog(binding.root.context, exerciseDetail)
                                recommendationDialog.show()
                            } else {
                                Log.d(TAG, "운동 상세 정보 없음")
                            }
                        } else {
                            Log.d(TAG, "운동 상세 정보 가져오기 실패")
                        }
                    }

                    override fun onFailure(call: Call<ResponseExerciseExerciseIdData>, t: Throwable) {
                        Log.e(TAG, "운동 상세 정보 요청 실패: $t")
                    }
                })
            }

        }



    }
    fun setAccessToken(token: String) {
        receivedAccessToken = token
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemRecommendationBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return recommendationList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(recommendationList[position],exerciseViewModel)

    }

    fun setData(newRecommendations: List<Exercise>) {
        val diffResult = DiffUtil.calculateDiff(ExerciseDiffCallback(recommendationList, newRecommendations))
        recommendationList = newRecommendations
        diffResult.dispatchUpdatesTo(this)
    }



    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    companion object {
        private const val TAG = "RecommendListFragment"
    }
}


