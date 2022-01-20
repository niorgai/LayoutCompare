package com.example.layoutcompare

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<View>(R.id.jump).setOnClickListener {
            startActivity(Intent(this, TraditionActivity::class.java))
        }

        findViewById<View>(R.id.jump2).setOnClickListener {
            val intent = Intent(this, TraditionActivity::class.java)
            intent.putExtra("constraint", true)
            startActivity(intent)
        }

        findViewById<View>(R.id.jump3).setOnClickListener {
            val intent = Intent(this, TraditionActivity::class.java)
            intent.putExtra("custom", true)
            startActivity(intent)
        }

        findViewById<View>(R.id.jump4).setOnClickListener {
            val intent = Intent(this, TraditionActivity::class.java)
            intent.putExtra("empty", true)
            startActivity(intent)
        }
    }
}