package com.raassh.gemastik15.di

import com.raassh.gemastik15.repository.*
import org.koin.dsl.module

val repositoryModule = module {
    single {
        AuthenticationRepository(get())
    }

    single {
        PlaceRepository(get(), get())
    }

    single {
        ContributionRepository(get())
    }

    single {
        ReadRepository(get())
    }

    single {
        ReportRepository(get())
    }
}