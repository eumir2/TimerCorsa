package com.example.timercorsa

import android.app.Activity
import android.media.RingtoneManager
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main);


    }

    fun timer(v: View) {
        val a: Activity = v.context as Activity

        val recupero = a.findViewById<EditText>(R.id.recupero).text.toString().toLong()
        val attivita = a.findViewById<EditText>(R.id.attivita).text.toString().toLong()
        val volte = a.findViewById<EditText>(R.id.volte).text.toString().toInt()

        val tempo = a.findViewById<EditText>(R.id.tempo)

        val sound = RingtoneManager.getRingtone(a.applicationContext, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
        val attivitaSound = RingtoneManager.getRingtone(a.applicationContext, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))

        val handler = Handler(Looper.getMainLooper())
        var currentCycle = 0

        // Funzione per gestire il timer di attività
        fun startAttivitaTimer() {
            val attivitaTimer = object : CountDownTimer(attivita * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    tempo.setText(formatMillisToMinutesSeconds(millisUntilFinished))
                }

                override fun onFinish() {
                    attivitaSound.play()
                    startRecuperoTimer()
                }
            }
            attivitaTimer.start()
        }

        // Funzione per gestire il timer di recupero
        fun startRecuperoTimer() {
            val recuperoTimer = object : CountDownTimer(recupero * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    tempo.setText(formatMillisToMinutesSeconds(millisUntilFinished))
                }

                override fun onFinish() {
                    sound.play()
                    currentCycle++
                    if (currentCycle < volte) {
                        handler.postDelayed({ startAttivitaTimer() }, 1000) // Aspetta un secondo prima di avviare il timer di attività
                    }
                }
            }
            recuperoTimer.start()
        }

        // Inizia il primo ciclo con il timer di attività
        startAttivitaTimer()
    }


    fun formatMillisToMinutesSeconds(millis: Long): String {
        val seconds = millis / 1000
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }
}