package com.raassh.gemastik15.di

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.raassh.gemastik15.local.db.PlaceDatabase
import com.raassh.gemastik15.local.preferences.UserPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localDataModule = module {
    single {
        PreferenceDataStoreFactory.create(
            produceFile = {
                androidContext().preferencesDataStoreFile("user_preferences")
            }
        )
    }

    single {
        UserPreferences(get())
    }

    factory {
        get<PlaceDatabase>().placeDao()
    }

    single {
        Room.databaseBuilder(androidContext(), PlaceDatabase::class.java, "places.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}