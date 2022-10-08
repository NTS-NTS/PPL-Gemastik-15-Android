package com.raassh.gemastik15.di

import com.raassh.gemastik15.repository.AuthenticationRepository
import com.raassh.gemastik15.repository.PlaceRepository
import org.koin.dsl.module

val repositoryModule = module {
    single {
        AuthenticationRepository(get())
    }

    single {
        PlaceRepository(get())
    }
}