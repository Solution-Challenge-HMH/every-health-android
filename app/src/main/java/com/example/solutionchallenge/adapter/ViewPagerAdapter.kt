package com.example.solutionchallenge.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.solutionchallenge.fragment.CalendarFragment

class ViewPagerAdapter (fragment : FragmentActivity, private val receivedAccessToken: String?) : FragmentStateAdapter(fragment){

    init {
        // accessToken이 잘 전달되었는지 로그로 확인
        Log.d("ViewPagerAdapter", "AccessToken: $receivedAccessToken")
    }

    override fun getItemCount(): Int = 1

    override fun createFragment(position: Int): Fragment {
        return CalendarFragment.newInstance(receivedAccessToken) //수정전 코드에 왜 CalendarFragment 임포트 안돼있었지?
    }
}
