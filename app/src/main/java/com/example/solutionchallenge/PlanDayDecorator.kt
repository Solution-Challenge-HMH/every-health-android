package com.example.solutionchallenge


import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import java.text.SimpleDateFormat
import java.time.LocalDate


class PlanDayDecorator(val context: Context, private val planDates: List<String>) : DayViewDecorator {

    private val highlightDrawable = context.resources.getDrawable(R.drawable.graycir)

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        // day가 null이면 장식할 필요 없음
        if (day == null) {
            return false
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd")
        val dateString = dateFormat.format(day.date)

        // planDates에 포함되어 있는지 확인하여 장식 여부를 결정
        return planDates.contains(dateString)
    }

    override fun decorate(view: DayViewFacade) {
        view.setSelectionDrawable(highlightDrawable)
        view.addSpan(ForegroundColorSpan(Color.BLACK))
       // view.addSpan(RelativeSizeSpan(1.5f))
    }
}
