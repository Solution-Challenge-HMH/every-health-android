package com.example.solutionchallenge.activity

import android.annotation.SuppressLint
import android.widget.Button

//mport androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.solutionchallenge.R
import com.example.solutionchallenge.serverdata.ServiceCreator
import com.example.solutionchallenge.serverdata.ResponseExerciseData
import com.example.solutionchallenge.fragment.BookmarkFragment
import com.google.firebase.auth.FirebaseAuth
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LogOutActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_out)

        val receivedAccessToken = intent.getStringExtra("receivedAccessToken")
        val nickname = intent.getStringExtra("nickname")
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbarButton(toolbar, receivedAccessToken)

        var logOutButton : Button =findViewById(R.id.logOutButton)
        var bookmarkButton : Button =findViewById(R.id.ToBookmarkButton)



        logOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            finish()
            val intent = Intent(this@LogOutActivity, LogInActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        bookmarkButton.setOnClickListener {
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
                            val bookmarkedExercises = exerciseList.filter { it.bookmarked }
                            val parcelableExerciseList =
                                ArrayList<Parcelable>(bookmarkedExercises.size)

                            bookmarkedExercises.forEach { exercise ->
                                parcelableExerciseList.add(exercise)
                                Log.d("Parceling Exercise", exercise.toString())
                            }

                            val fragment = receivedAccessToken?.let { it1 ->
                                BookmarkFragment(it1).apply {
                                    arguments = Bundle().apply {
                                        // 운동 목록 데이터를 Bundle에 넣어 전달
                                        putParcelableArrayList("exerciseList", parcelableExerciseList)
                                    }
                                }
                            }

                            if (fragment != null) {
                                supportFragmentManager.beginTransaction()
                                    .replace(R.id.fragment_container2, fragment)
                                    .addToBackStack(null) // 백 스택에 추가
                                    .commit()
                            }

                        } else {
                            Log.d(TAG, "운동 데이터 없음")
                        }
                    } else {
                        Log.d(TAG, "운동 데이터 가져오기 실패")
                    }
                }

                override fun onFailure(call: Call<ResponseExerciseData>, t: Throwable) {
                    Log.e(TAG, "네트워크 요청 실패: $t")
                }
            })
        }
        /*
        bookmarkButton.setOnClickListener {

            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container2, BookmarkFragment(
                receivedAccessToken!!
            ))
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
*/
    }

    /*
    val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
    toolbarButton(toolbar)
 */
    fun toolbarButton(toolbar: androidx.appcompat.widget.Toolbar, receivedAccessToken: String?){

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.ToCalendarButtonInMenu -> { // 메뉴 아이템의 ID에 따라 동작을 결정합니다.
                    // 다음 activity로 이동하는 코드를 작성합니다.
                    val intent = Intent(this, CalendarActivity::class.java)
                    intent.putExtra("receivedAccessToken", receivedAccessToken)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.ToMypageButtonInMenu -> { // 메뉴 아이템의 ID에 따라 동작을 결정합니다.
                    // 다음 activity로 이동하는 코드를 작성합니다.
                    val intent = Intent(this, LogOutActivity::class.java)
                    intent.putExtra("receivedAccessToken", receivedAccessToken)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.ToMainButtonInMenu -> { // 메뉴 아이템의 ID에 따라 동작을 결정합니다.
                    // 다음 activity로 이동하는 코드를 작성합니다.
                    val intent = Intent(this, MainActivity::class.java)
                    intent.putExtra("receivedAccessToken", receivedAccessToken)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    companion object {
        private const val TAG = "ExerciseAdapter"
    }

}

