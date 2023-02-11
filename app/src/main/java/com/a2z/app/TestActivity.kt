package com.a2z.app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.a2z_di.app.activity.login.LoginActivity

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        Intent(this,LoginActivity::class.java).apply {
            startActivity(this)
        }
    }
}