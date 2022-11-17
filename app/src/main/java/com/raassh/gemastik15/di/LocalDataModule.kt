package com.raassh.gemastik15.di

import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.raassh.gemastik15.local.db.KiadDatabase
import com.raassh.gemastik15.local.preferences.SettingPreferences
import com.raassh.gemastik15.local.preferences.UserPreferences
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val localDataModule = module {
    single {
        UserPreferences(PreferenceDataStoreFactory.create(
            produceFile = {
                androidContext().preferencesDataStoreFile("user_preferences")
            }
        ))
    }

    single {
        SettingPreferences(PreferenceDataStoreFactory.create(
            produceFile = {
                androidContext().preferencesDataStoreFile("setting_preferences")
            }
        ))
    }

    factory {
        get<KiadDatabase>().placeDao()
    }

    factory {
        get<KiadDatabase>().chatDao()
    }

    single {
        Room.databaseBuilder(androidContext(), KiadDatabase::class.java, "places.db")
            .fallbackToDestructiveMigration()
            .build()
    }
}