package com.raassh.gemastik15.local.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [PlaceEntity::class, ChatEntity::class, MessageEntity::class], version = 2, exportSchema = false)
abstract class KiadDatabase : RoomDatabase() {
    abstract fun placeDao(): PlaceDao

    abstract fun chatDao(): ChatDao
}