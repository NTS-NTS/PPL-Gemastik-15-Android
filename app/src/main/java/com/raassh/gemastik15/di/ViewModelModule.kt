package com.raassh.gemastik15.di

import com.raassh.gemastik15.view.activity.dashboard.DashboardViewModel
import com.raassh.gemastik15.view.activity.main.MainActivityViewModel
import com.raassh.gemastik15.view.dialogs.ChooseDisabilityDialogViewModel
import com.raassh.gemastik15.view.fragments.account.AccountViewModel
import com.raassh.gemastik15.view.fragments.addcontribution.AddContributionViewModel
import com.raassh.gemastik15.view.fragments.changedisability.ChangeDisabilityViewModel
import com.raassh.gemastik15.view.fragments.changepassword.ChangePasswordViewModel
import com.raassh.gemastik15.view.fragments.contribution.ContributionViewModel
import com.raassh.gemastik15.view.fragments.detailcontributionreport.DetailContributionReportViewModel
import com.raassh.gemastik15.view.fragments.detailuserreport.DetailUserReportViewModel
import com.raassh.gemastik15.view.fragments.discover.DiscoverViewModel
import com.raassh.gemastik15.view.fragments.editcontribution.EditContributionViewModel
import com.raassh.gemastik15.view.fragments.editprofile.EditProfileViewModel
import com.raassh.gemastik15.view.fragments.landing.LandingViewModel
import com.raassh.gemastik15.view.fragments.login.LoginViewModel
import com.raassh.gemastik15.view.fragments.moderation.ModerationViewModel
import com.raassh.gemastik15.view.fragments.placedetail.PlaceDetailViewModel
import com.raassh.gemastik15.view.fragments.read.ReadViewModel
import com.raassh.gemastik15.view.fragments.register.RegisterViewModel
import com.raassh.gemastik15.view.fragments.reportreview.ReportReviewViewModel
import com.raassh.gemastik15.view.fragments.reviewhistory.ReviewHistoryViewModel
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
        LoginViewModel(get(), get(), get())
    }

    viewModel {
        RegisterViewModel(get())
    }

    viewModel {
        DiscoverViewModel(get(), get())
    }

    viewModel {
        MainActivityViewModel(get(), get())
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
        PlaceDetailViewModel(get(), get())
    }

    viewModel {
        AddContributionViewModel(get())
    }

    viewModel {
        ContributionViewModel(get(), get())
    }

    viewModel {
        AccountViewModel(get(), get(), get())
    }

    viewModel {
        ChooseDisabilityDialogViewModel(get(), get())
    }

    viewModel {
        ChangePasswordViewModel(get())
    }

    viewModel {
        EditProfileViewModel(get(), get())
    }

    viewModel {
        ReadViewModel(get())
    }

    viewModel {
        ModerationViewModel(get())
    }

    viewModel {
        DetailContributionReportViewModel(get())
    }

    viewModel {
        DetailUserReportViewModel(get())
    }

    viewModel {
        ReportReviewViewModel(get())
    }

    viewModel {
        EditContributionViewModel(get())
    }

    viewModel {
        ReviewHistoryViewModel(get())
    }

    viewModel {
        ChangeDisabilityViewModel(get(), get())
    }
}