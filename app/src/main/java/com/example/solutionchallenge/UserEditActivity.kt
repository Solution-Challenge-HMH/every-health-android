package com.example.solutionchallenge

import android.content.Context
import android.content.res.ColorStateList
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


class UserEditActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUserEditBinding

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
        val colorRes = if (isBlue) android.R.color.holo_blue_light else android.R.color.holo_red_light
        button.setBackgroundColor(ContextCompat.getColor(this, colorRes))
    }




    private fun saveUserData() {
        val nickname = binding.nicknameEditText.text.toString()
        val gender = getGender()
        val physicalAbilityLevel = getPhysical_ability_level()

        if (nickname.isNotEmpty() && gender.isNotEmpty() && physicalAbilityLevel.isNotEmpty()) {
            // 모든 정보가 입력된 경우에만 저장 수행
            with(getSharedPreferences("userInformation", Context.MODE_PRIVATE).edit()) {
                putString(NICKNAME, nickname)
                putString(GENDER, gender)
                putString(PHYSICAL_ABILITY_LEVEL, physicalAbilityLevel)

                apply()
            }

            //저장된 정보 출력
            Log.d("UserEditActivity", "User information saved: " +
                    "Nickname: $nickname, " +
                    "Gender: $gender, " +
                    "Physical Ability Level: $physicalAbilityLevel"
            )
            for (buttonId in buttonIds) {
                val buttonName = resources.getResourceEntryName(buttonId)
                val statusMessage = "버튼 ID: $buttonName, 상태: ${buttonStateMap[buttonId]}"
                Log.d("UserEditActivity", statusMessage)
            }
        }
        else { // 닉네임, 성별, 신체 기능 중 뭔가 입력 안 했을 때
            // 사용자에게 모든 정보를 입력하라는 Toast 메시지 표시
            Toast.makeText(this, "모든 정보를 입력하세요.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getGender(): String {
        val gender = if(binding.GenderMale.isChecked) "남" else "여"
        return "$gender"
    }

    private fun getPhysical_ability_level(): String {
        val radioGroup: RadioGroup = findViewById(R.id.physical_ability_level_group)
        val checkedRadioButtonId: Int = radioGroup.checkedRadioButtonId

        return when (checkedRadioButtonId) {
            R.id.impossibleToSit -> "앉기 불가능"
            R.id.possibleToSit -> "앉기 가능"
            R.id.possibleToStand -> "서기 가능"
            R.id.possibleToWalk -> "걷기 가능"
            R.id.possibleToRun -> "달리기 가능"
            else -> "아무것도 선택되지 않음"+ Toast.makeText(this, "신체 기능을 선택해주세요.", Toast.LENGTH_SHORT).show().toString()
        }
    }
}
