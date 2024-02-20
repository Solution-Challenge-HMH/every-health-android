package com.example.solutionchallenge.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import androidx.appcompat.app.AppCompatActivity
import com.example.solutionchallenge.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.example.solutionchallenge.calendar.ViewPagerAdapter

import com.example.solutionchallenge.databinding.CalendarActivityMainBinding



class CalendarActivity : AppCompatActivity() {

    private lateinit var binding : CalendarActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar_activity_main)


        binding = CalendarActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val receivedAccessToken = intent.getStringExtra("receivedAccessToken")
        Log.d("calact","$receivedAccessToken")

        // 뷰페이저에 어댑터 연결
        binding.pager.adapter = ViewPagerAdapter(this, receivedAccessToken)

        //toolbarButton()
    }



    /*
    val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
    toolbarButton(toolbar)
 */
    fun toolbarButton(toolbar: androidx.appcompat.widget.Toolbar){

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.ToCalendarButtonInMenu -> { // 메뉴 아이템의 ID에 따라 동작을 결정합니다.
                    // 다음 activity로 이동하는 코드를 작성합니다.
                    val intent = Intent(this, CalendarActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.ToMypageButtonInMenu -> { // 메뉴 아이템의 ID에 따라 동작을 결정합니다.
                    // 다음 activity로 이동하는 코드를 작성합니다.
                    val intent = Intent(this, LogOutActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                R.id.ToMainButtonInMenu -> { // 메뉴 아이템의 ID에 따라 동작을 결정합니다.
                    // 다음 activity로 이동하는 코드를 작성합니다.
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }
    }




}