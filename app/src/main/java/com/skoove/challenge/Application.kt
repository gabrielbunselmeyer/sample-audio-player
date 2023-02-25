package com.skoove.challenge

import android.app.Application
import com.skoove.challenge.di.Modules

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        Modules.init(this)
    }
}
