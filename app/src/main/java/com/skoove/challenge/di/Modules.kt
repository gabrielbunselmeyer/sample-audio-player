package com.skoove.challenge.di

import android.app.Application
import com.skoove.challenge.ui.State
import com.skoove.challenge.ui.audiodetail.AudioDetailViewModel
import com.skoove.challenge.ui.audiolist.AudioListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.dsl.module

object Modules {
    fun init(context: Application) {
        startKoin {
            androidContext(context)
            loadKoinModules(com.skoove.challenge.domain.di.Modules.common)
            loadKoinModules(com.skoove.challenge.data.di.Modules.common)
            loadKoinModules(main)
        }
    }

    private val main = module {
        single { State() }

        viewModel { AudioListViewModel(application = get(), skooveRepository = get(), state = get()) }
        viewModel { AudioDetailViewModel(state = get()) }
    }
}
