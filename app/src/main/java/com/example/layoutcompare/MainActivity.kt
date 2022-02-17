package com.example.layoutcompare

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.jump).setOnClickListener {
            startActivityForResult(Intent(this, TestActivity::class.java), 1)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            val content = data?.getStringExtra("result") ?: ""
            if (content.isNotEmpty()) {
                findViewById<TextView>(R.id.result).setText(content)
            }
        }
    }
}