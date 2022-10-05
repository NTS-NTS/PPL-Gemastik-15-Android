package com.raassh.gemastik15.di

import com.raassh.gemastik15.view.activity.main.MainActivityViewModel
import com.raassh.gemastik15.view.fragments.discover.DiscoverViewModel
import com.raassh.gemastik15.view.fragments.landing.LandingViewModel
import com.raassh.gemastik15.view.fragments.login.LoginViewModel
import com.raassh.gemastik15.view.fragments.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        LandingViewModel()
    }

    viewModel {
        LoginViewModel(get(), get())
    }

    viewModel {
        RegisterViewModel(get())
    }

    viewModel {
        DiscoverViewModel()
    }

    viewModel {
        MainActivityViewModel(get())
    }
}