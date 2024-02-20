package com.example.solutionchallenge.activity

import android.widget.Button

//mport androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.solutionchallenge.R
import com.example.solutionchallenge.databinding.ActivityLogInBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LogOutActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_out)

        val receivedAccessToken = intent.getStringExtra("receivedAccessToken")

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        toolbarButton(toolbar, receivedAccessToken)

        var logOutButton : Button =findViewById(R.id.logOutButton)

        logOutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
        }



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
}

