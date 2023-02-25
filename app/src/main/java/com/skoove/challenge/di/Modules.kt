package com.skoove.challenge.di

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.dsl.module

/// This modules main DI configuration
object Modules {

    /// Main trigger to initializes this modules dependencies
    fun init(context: Application) {
        startKoin {
            androidContext(context)
            loadKoinModules(com.skoove.challenge.domain.di.Modules.common)
            loadKoinModules(com.skoove.challenge.data.di.Modules.common)
            loadKoinModules(main)
        }
    }

    /// Main declaration for this modules dependencies
    private val main = module {
    }
}
