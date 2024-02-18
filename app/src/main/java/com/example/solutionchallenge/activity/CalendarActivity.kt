package com.example.solutionchallenge.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.example.solutionchallenge.calendar.ViewPagerAdapter

import com.example.solutionchallenge.databinding.CalendarActivityMainBinding



class CalendarActivity : AppCompatActivity() {


    private lateinit var binding : CalendarActivityMainBinding
    //private val calendarDayList = ArrayList<CalendarDay>() //added

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CalendarActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val receivedAccessToken = intent.getStringExtra("receivedAccessToken")
        Log.d("calact","$receivedAccessToken")
        val activity = this //added

        // 뷰페이저에 어댑터 연결
        binding.pager.adapter = ViewPagerAdapter(this, receivedAccessToken)



    }


}