package com.hyperone.assignment.room

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * The Room database that contains the users table.
 * This is the class that
 *
 * @author Abdelaziz Daoud
 * @since 11/05/2022
 */
@Database(entities = [Source::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sourceDao(): SourceDao
}