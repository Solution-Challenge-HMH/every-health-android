package com.example.solutionchallenge.decorator


import android.graphics.Color
import android.graphics.Typeface
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import com.example.solutionchallenge.R
import com.example.solutionchallenge.fragment.CalendarFragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.DayViewDecorator

class OneDayDecorator(private val context: CalendarFragment) : DayViewDecorator {

    private val highlightDrawable = context.resources.getDrawable(R.drawable.pinkcir)

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == CalendarDay.today()
    }

    override fun decorate(view: DayViewFacade) {
        view.setBackgroundDrawable(highlightDrawable)
        view.addSpan(ForegroundColorSpan(Color.WHITE))
        view.addSpan(StyleSpan(Typeface.BOLD))
        view.addSpan(RelativeSizeSpan(1.2f))
    }
}
