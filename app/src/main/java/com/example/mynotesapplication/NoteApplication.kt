package com.example.mynotesapplication

import android.app.Application
import com.example.mynotesapplication.backend.data.AppContainer
import com.example.mynotesapplication.backend.data.DefaultAppContainer
import kotlin.math.log


class NoteApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
    }
}