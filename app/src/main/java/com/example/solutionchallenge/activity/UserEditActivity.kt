package com.example.solutionchallenge.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.solutionchallenge.R
import com.example.solutionchallenge.databinding.ActivityUserEditBinding
import com.example.solutionchallenge.Constants.buttonIds
import com.example.solutionchallenge.GENDER
import com.example.solutionchallenge.NICKNAME
import com.example.solutionchallenge.PHYSICAL_ABILITY_LEVEL
import com.example.solutionchallenge.ServiceCreator
import com.example.solutionchallenge.datamodel.RequestUserInfoData
import com.example.solutionchallenge.datamodel.ResponseUserInfoData
import com.example.solutionchallenge.datamodel.ResponseUserLoginData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class UserEditActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserEditBinding
    private val buttonStateMap = mutableMapOf<Int, Boolean>() // 버튼 상태를 저장하는 맵

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserEditBinding.inflate(layoutInflater)
        setContentView(binding.root)



        for (buttonId in buttonIds) {
            buttonStateMap[buttonId] = true // 초기 상태: 핑크색
            val colorButton = binding.root.findViewById<Button>(buttonId)
            setButtonColor(colorButton, true)
            setButtonClickListener(colorButton)
        }

        binding.userinfoSaveButton.setOnClickListener {

            saveUserData()

        }
    }

    private fun setButtonClickListener(button: Button) {
        button.setOnClickListener {
            val buttonId = button.id
            val isBlue = !buttonStateMap[buttonId]!!
            buttonStateMap[buttonId] = isBlue
            setButtonColor(button, isBlue)
        }
    }

    private fun setButtonColor(button: Button, isBlue: Boolean) {
        val colorRes =
            if (isBlue) android.R.color.holo_blue_light else R.color.pink
        button.setBackgroundColor(ContextCompat.getColor(this, colorRes))
    }


    private fun saveUserData() {
        val receivedAccessToken = intent.getStringExtra("receivedAccessToken")

        val nickname = binding.nicknameEditText.text.toString()
        val gender = getGender()
        val physicalAbilityLevel = getPhysical_ability_level()
        val core = buttonStateMap[R.id.core] ?: false
        val rightUpperArm = buttonStateMap[R.id.right_upper_arm_button] ?: false
        val rightLowerArm = buttonStateMap[R.id.right_upper_arm_button] ?: false
        val leftUpperArm = buttonStateMap[R.id.right_upper_arm_button] ?: false
        val leftLowerArm = buttonStateMap[R.id.left_lower_arm_button] ?: false
        val rightUpperLeg = buttonStateMap[R.id.right_upper_leg_button] ?: false
        val rightLowerLeg = buttonStateMap[R.id.right_upper_leg_button] ?: false
        val leftUpperLeg = buttonStateMap[R.id.right_upper_leg_button] ?: false
        val leftLowerLeg = buttonStateMap[R.id.left_lower_leg_button] ?: false


        val requestUserInfoData = RequestUserInfoData(
            nickname,
            gender,
            physicalAbilityLevel,
            core,
            rightUpperArm,
            rightLowerArm,
            leftUpperArm,
            leftLowerArm,
            rightUpperLeg,
            rightLowerLeg,
            leftUpperLeg,
            leftLowerLeg
        )
        val call: Call<ResponseUserInfoData> =
            ServiceCreator.everyHealthService.postUserInfo(
                "Bearer $receivedAccessToken",
                requestUserInfoData
            )


        if (nickname.isNotEmpty() && gender.isNotEmpty() && physicalAbilityLevel.isNotEmpty()) {
            call.enqueue(object : Callback<ResponseUserInfoData> {
                override fun onResponse(
                    call: Call<ResponseUserInfoData>,
                    response: Response<ResponseUserInfoData>
                ) {
                    if (response.isSuccessful) {
                        //유저 신체 정보 전송 성공
                        Log.d(TAG, "유저 신체 정보 전송 성공")

                        val intent = Intent(this@UserEditActivity, LogOutActivity::class.java)
                        intent.putExtra("nickname", nickname)
                        Log.d("nickname", "$nickname")
                        val mainIntent = Intent(this@UserEditActivity, MainActivity::class.java)
                        mainIntent.putExtra("receivedAccessToken", receivedAccessToken)
                        startActivity(mainIntent)
                    } else {
                        Log.d(TAG, "유저 신체 정보 전송 실패")
                    }

                }

                override fun onFailure(call: Call<ResponseUserInfoData>, t: Throwable) {
                    // 통신 실패 시 처리
                    Log.e("NetworkTest", "error:$t")
                }
            })


        } else { // 닉네임, 성별, 신체 기능 중 뭔가 입력 안 했을 때
            // 사용자에게 모든 정보를 입력하라는 Toast 메시지 표시
            Toast.makeText(this, "모든 정보를 입력하세요.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getGender(): String {
        val gender =
            if (binding.GenderFemale.isChecked) "F" else if (binding.GenderNone.isChecked) "N" else "M"
        return "$gender"
    }

    private fun getPhysical_ability_level(): String {
        val radioGroup: RadioGroup = findViewById(R.id.physical_ability_level_group)
        val checkedRadioButtonId: Int = radioGroup.checkedRadioButtonId

        return when (checkedRadioButtonId) {
            R.id.impossibleToSit -> "UNABLE_TO_SIT"
            R.id.possibleToSit -> "ABLE_TO_SIT"
            R.id.possibleToStand -> "ABLE_TO_STAND"
            R.id.possibleToWalk -> "ABLE_TO_WALK"
            R.id.possibleToRun -> "ABLE_TO_RUN"
            else -> {
                Toast.makeText(this, "신체 기능을 선택해주세요.", Toast.LENGTH_SHORT).show()
                "Unable to sit" // 기본값
            }
        }
    }

    companion object {
        const val TAG = "UserEditActivity"
    }
}
