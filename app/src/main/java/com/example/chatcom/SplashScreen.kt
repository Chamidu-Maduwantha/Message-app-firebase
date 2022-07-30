package com.example.chatcom

//import android.support.v7.app.AppCompatActivity
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class SplashScreen : AppCompatActivity() {

    //private var Splash_screen_time: Long = 3500

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )*/
        setContentView(R.layout.activity_splash_screen)

    prog()

    }

    private fun prog(){

        val prgbar: ProgressBar = findViewById(R.id.progressBar)

        val intent = Intent (this,LogIn::class.java)
        var count:Int = 0
        val timer = Timer()
        with(timer) {
            schedule(object: TimerTask() {
                override fun run() {
                    count++
                    prgbar.progress = count
                    if (count > 50) {
                        cancel()
                        startActivity(intent)
                        finish()
                    }

                }
            } ,0, 30
            )

        }
    }

}