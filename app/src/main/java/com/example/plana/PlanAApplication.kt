package com.example.plana

import android.app.Application
import com.example.plana.di.AppContainer

class PlanAApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
