package com.raassh.gemastik15.di

import com.raassh.gemastik15.view.activity.dashboard.DashboardViewModel
import com.raassh.gemastik15.view.activity.main.MainActivityViewModel
import com.raassh.gemastik15.view.fragments.AddContribution.AddContributionViewModel
import com.raassh.gemastik15.view.fragments.discover.DiscoverViewModel
import com.raassh.gemastik15.view.fragments.landing.LandingViewModel
import com.raassh.gemastik15.view.fragments.login.LoginViewModel
import com.raassh.gemastik15.view.fragments.placeDetail.PlaceDetailViewModel
import com.raassh.gemastik15.view.fragments.register.RegisterViewModel
import com.raassh.gemastik15.view.fragments.searchbyfacility.SearchByFacilityViewModel
import com.raassh.gemastik15.view.fragments.searchfacilityoption.SearchFacilityOptionViewModel
import com.raassh.gemastik15.view.fragments.searchresult.SearchResultViewModel
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
        DiscoverViewModel(get())
    }

    viewModel {
        MainActivityViewModel(get())
    }

    viewModel {
        SearchResultViewModel(get())
    }

    viewModel {
        SearchFacilityOptionViewModel()
    }

    viewModel {
        SearchByFacilityViewModel(get())
    }

    viewModel {
        DashboardViewModel(get())
    }

    viewModel {
        PlaceDetailViewModel()
    }

    viewModel {
        AddContributionViewModel()
    }
}