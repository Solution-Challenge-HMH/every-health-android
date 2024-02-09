package com.example.solutionchallenge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import com.example.solutionchallenge.databinding.ActivityStopWatchBinding
import com.example.solutionchallenge.databinding.DialogCountdownSettingBinding
import java.util.Timer
import kotlin.concurrent.timer

class StopWatchActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStopWatchBinding
    private var countdownSecond = 5
    private var currentCountdownDeciSecond = countdownSecond * 10
    private var currentDeciSecond = 0 // 0.1초 단위의 숫자
    private var timer: Timer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStopWatchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.countdownTextView.setOnClickListener {
            showCountdownSettingDialog()

        }

        binding.startButton.setOnClickListener {
            start()
            binding.startButton.isVisible = false
            binding.stopButton.isVisible = false
            binding.pauseButton.isVisible = true
            binding.lapButton.isVisible = true


        }
        binding.stopButton.setOnClickListener {
            showAlertDialog()

        }
        binding.pauseButton.setOnClickListener {
            pause()
            binding.startButton.isVisible = true
            binding.stopButton.isVisible = true
            binding.pauseButton.isVisible = false
            binding.lapButton.isVisible = false

        }
        binding.lapButton.setOnClickListener {
            lap()
            binding.startButton.isVisible = false
            binding.stopButton.isVisible = false
            binding.pauseButton.isVisible = true
            binding.lapButton.isVisible = true

        }
        initCountdownViews()
    }

    private fun initCountdownViews() {
        binding.countdownTextView.text = String.format("%02d", countdownSecond)
        binding.countdownProgressBar.progress = 100
    }

    private fun start() {

        timer = timer(initialDelay = 0, period = 100) {
            if (currentCountdownDeciSecond == 0) {

                currentDeciSecond += 1
                //Log.d("currentDeciSecond", currentDeciSecond.toString())
                val hours = currentDeciSecond.div(10) / 60 / 60
                val minutes = currentDeciSecond.div(10) / 60
                val seconds = currentDeciSecond.div(10) % 60
                val deciSeconds = currentDeciSecond % 10

                runOnUiThread {
                    binding.timeTextView.text =
                        String.format("%02d:%02d:%02d", hours, minutes, seconds)
                    binding.tickTextView.text = deciSeconds.toString()

                    binding.countdownGroup.isVisible = false
                }
            } else {
                currentCountdownDeciSecond -= 1
                val seconds = currentCountdownDeciSecond / 10
                val progress = (currentCountdownDeciSecond / (countdownSecond * 10f)) * 100
                runOnUiThread {
                    binding.countdownTextView.text = String.format("%02d", seconds)
                    binding.countdownProgressBar.progress = progress.toInt()
                }

            }


        }

    }

    private fun stop() {
        binding.startButton.isVisible = true
        binding.stopButton.isVisible = true
        binding.pauseButton.isVisible = false
        binding.lapButton.isVisible = false

        currentDeciSecond = 0
        binding.timeTextView.text = "00:00:00"
        binding.tickTextView.text = "0"

        binding.countdownGroup.isVisible = true
        initCountdownViews()
        binding.lapContainerLinearLayout.removeAllViews()

    }

    private fun pause() {
        timer?.cancel()
        timer = null
    }

    private fun lap() {
        if (currentDeciSecond == 0) return

        val container = binding.lapContainerLinearLayout
        val lapTextView = TextView(this).apply {
            textSize = 15f
            gravity = Gravity.CENTER
            val hours = currentDeciSecond.div(10) / 60 / 60
            val minutes = currentDeciSecond.div(10) / 60
            val seconds = currentDeciSecond.div(10) % 60
            text = container.childCount.inc().toString() + String.format(
                ") %02d:%02d:%02d", hours, minutes, seconds
            ) // 스트링 표현 형식: 1. 01:30:06 6

            setPadding(30)
        }.let { lapTextView ->
            container.addView(lapTextView, 0)

        }

    }

    private fun showCountdownSettingDialog() {
        AlertDialog.Builder(this).apply {
            val dialogBinding = DialogCountdownSettingBinding.inflate(layoutInflater)
            with(dialogBinding.countdownSecondPicker) {
                maxValue = 20
                minValue = 0
                value = countdownSecond
            }
            setView(dialogBinding.root)
            setTitle("카운트다운 설정")
            setPositiveButton("확인") { _, _ ->
                countdownSecond = dialogBinding.countdownSecondPicker.value
                currentCountdownDeciSecond = countdownSecond * 10
                binding.countdownTextView.text = String.format("%02d", countdownSecond)
            }
            setNegativeButton("취소", null)
        }.show()
    }

    private fun showAlertDialog() {
        AlertDialog.Builder(this).apply {
            setMessage("종료하시겠습니까?")
            setPositiveButton("네") { _, _ ->
                stop()
            }
            setNegativeButton("아니오", null)
        }.show()
    }
}