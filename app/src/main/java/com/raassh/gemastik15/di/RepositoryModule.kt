package com.raassh.gemastik15.di

import com.raassh.gemastik15.repository.AuthenticationRepository
import org.koin.dsl.module

val repositoryModule = module {
    single {
        AuthenticationRepository(get())
    }
}