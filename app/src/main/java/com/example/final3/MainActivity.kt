package com.example.final3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.final3.databinding.ActivityMainBinding
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var timer: CountDownTimer? = null
    private var isFormatting = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        // Добавляем TextWatcher для поля ввода времени
        binding.timeEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (!isFormatting) {
                    isFormatting = true
                    if (s?.length == 2 && s.toString().indexOf(":") == -1) {
                        s.insert(2, ":")
                    }
                    if (s?.length == 5 && s.toString().indexOf(":") == 2) {
                        binding.timerTextView.text="Введите корректное время"
                    }
                    isFormatting = false
                }
            }
        })

        binding.startButton.setOnClickListener {
            startTimer()
        }

        binding.restartButton.setOnClickListener {
            restartTimer()
        }
    }

    private fun startTimer() {
        val inputTime = binding.timeEditText.text.toString().split(":")
        if (inputTime.size == 2) {
            val minutes = inputTime[0].toLongOrNull()
            val seconds = inputTime[1].toLongOrNull()

            if (minutes != null) {
                var seconds = inputTime[1].toLongOrNull()

                if (seconds == null || seconds > 59) {
                    seconds = 59
                }

                val totalTimeInSeconds = (minutes * 60) + seconds
                timer?.cancel()
                timer = object : CountDownTimer(totalTimeInSeconds * 1000, 1000) {
                    override fun onTick(millisUntilFinished: Long) {
                        val minutesRemaining = millisUntilFinished / 1000 / 60
                        val secondsRemaining = (millisUntilFinished / 1000) % 60
                        binding.timerTextView.text = String.format(
                            "Осталось времени: %02d:%02d",
                            minutesRemaining,
                            secondsRemaining
                        )
                    }
                    override fun onFinish() {
                        binding.timerTextView.text = "Таймер завершен!"
                        binding.restartButton.visibility = View.VISIBLE
                    }
                }
                timer?.start()
                binding.restartButton.visibility = View.VISIBLE
            } else {
                binding.timerTextView.text = "Пожалуйста, введите корректное время (мм:сс)."
            }
        } else {
            binding.timerTextView.text = "Пожалуйста, введите время в формате (мм:сс)."
        }
    }

    private fun restartTimer() {
        timer?.cancel()
        binding.timerTextView.text = ""
        binding.timeEditText.text.clear()
        binding.restartButton.visibility = View.INVISIBLE
        startTimer()
    }
}