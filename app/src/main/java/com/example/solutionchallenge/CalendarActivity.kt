package com.example.solutionchallenge

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.myfirstandroidapp.helpcalendar.ViewPagerAdapter

//import com.myfirstandroidapp.helpcalendar.databinding.ActivityMainBinding
import com.example.solutionchallenge.databinding.CalendarActivityMainBinding
import com.prolificinteractive.materialcalendarview.CalendarDay


class CalendarActivity : AppCompatActivity() {
    private lateinit var binding : CalendarActivityMainBinding
    private val calendarDayList = ArrayList<CalendarDay>() //added

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CalendarActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val activity = this //added

        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                // 탭이 선택 되었을 때
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // 탭이 선택되지 않은 상태로 변경 되었을 때
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // 이미 선택된 탭이 다시 선택 되었을 때
            }
        })

        // 뷰페이저에 어댑터 연결
        binding.pager.adapter = ViewPagerAdapter(this)


        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when(position) {
                0 -> tab.text = "Calendar"
            }
        }.attach()

        /*

        //색칠할 날짜를 calendarDayList에 추가
        calendarDayList.add(CalendarDay.from(2022, 5, 25))
        calendarDayList.add(CalendarDay.from(2022, 5, 24))
        calendarDayList.add(CalendarDay.from(2022, 5, 23))


        // 오늘날짜 선택
        //calendar.setSelectedDate(CalendarDay.today()) //added

        // 데코레이터
        val decorator = Decorator(calendarDayList, activity)
        binding.calendarView.addDecorator(decorator)

         */
    }


}