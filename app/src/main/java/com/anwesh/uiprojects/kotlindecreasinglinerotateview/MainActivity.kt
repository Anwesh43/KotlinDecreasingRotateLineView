package com.anwesh.uiprojects.kotlindecreasinglinerotateview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.decreasinglinerotateview.DecreasingLineRotateView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DecreasingLineRotateView.create(this)
    }
}
