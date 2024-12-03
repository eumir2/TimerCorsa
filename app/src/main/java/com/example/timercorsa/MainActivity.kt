package com.example.timercorsa

import SoundPlayer
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)


    }

    fun timer(v: View) {
        val a: Activity = v.context as Activity

        val recupero = a.findViewById<EditText>(R.id.recupero).text.toString().toLong()
        val attivita = a.findViewById<EditText>(R.id.attivita).text.toString().toLong()
        val counter = a.findViewById<EditText>(R.id.volte)
        counter.isEnabled = false
        val volte = counter.text.toString().toInt()
        val current = a.findViewById<TextView>(R.id.current)

        val tempo = a.findViewById<EditText>(R.id.tempo)

        val corsaUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.run)
        val recuperoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.recupero)
        val completeUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.finish)

        val soundPlayer = SoundPlayer(v.context)

        /*
        val run = RingtoneManager.getRingtone(a.applicationContext, corsaUri)
        val rest = RingtoneManager.getRingtone(a.applicationContext, recuperoUri)
        val complete = RingtoneManager.getRingtone(a.applicationContext, completeUri)
        //val attivitaSound = RingtoneManager.getRingtone(a.applicationContext, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM))
        */
        val handler = Handler(Looper.getMainLooper())
        var currentCycle = 0

        // Funzione per gestire il timer di attività
        fun startAttivitaTimer() {
            val attivitaTimer = object : CountDownTimer(attivita * 1000, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    tempo.setText(formatMillisToMinutesSeconds(millisUntilFinished))
                }

                override fun onFinish() {
                    soundPlayer.playSound(recuperoUri)
                    current.setText("Recupero")
                    startRecuperoTimer()
                }

                //Funzione per gestire il timer del recupero.
                private fun startRecuperoTimer() {
                    val recuperoTimer = object : CountDownTimer(recupero * 1000, 1000) {
                        override fun onTick(millisUntilFinished: Long) {
                            tempo.setText(formatMillisToMinutesSeconds(millisUntilFinished))
                        }

                        override fun onFinish() {
                            currentCycle++
                            if (currentCycle < volte) {
                                current.setText("Attività")
                                counter.setText((volte-currentCycle).toString())
                                soundPlayer.playSound(corsaUri)
                                handler.postDelayed({ startAttivitaTimer() }, 5000) // Aspetta 5 secondi prima di avviare il timer di attività
                            }else{
                                counter.setText("Finish!!!")
                                current.setText("Finish!!!")
                                soundPlayer.playSound(completeUri)
                            }
                        }
                    }
                    recuperoTimer.start()
                }
            }
            attivitaTimer.start()
        }

        // Inizia il primo ciclo con il timer di attività
        current.setText("Attività")
        soundPlayer.playSound(corsaUri)
        handler.postDelayed({
            startAttivitaTimer()}, 5000)



    }

    //formattazione dei millisecondi in formato HH:MM:SS
    fun formatMillisToMinutesSeconds(millis: Long): String {
        val seconds = millis / 1000
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }
}