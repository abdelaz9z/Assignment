package com.hyperone.assignment.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Source::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sourceDao(): SourceDao
}