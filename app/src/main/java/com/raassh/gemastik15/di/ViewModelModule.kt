package com.raassh.gemastik15.di

import com.raassh.gemastik15.view.discover.DiscoverViewModel
import com.raassh.gemastik15.view.landing.LandingViewModel
import com.raassh.gemastik15.view.login.LoginViewModel
import com.raassh.gemastik15.view.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        LandingViewModel()
    }

    viewModel {
        LoginViewModel(get())
    }

    viewModel {
        RegisterViewModel(get())
    }

    viewModel {
        DiscoverViewModel()
    }
}