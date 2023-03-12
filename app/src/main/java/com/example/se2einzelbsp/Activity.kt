package com.example.se2einzelbsp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

open class Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}