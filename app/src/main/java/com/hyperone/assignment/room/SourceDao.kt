package com.hyperone.assignment.room

import androidx.room.*

/**
 * The interface for the source dao.
 *
 * @author Abdelaziz Daoud
 * @since 11/05/2022
 */
@Dao
interface SourceDao {

    @Query("SELECT * FROM source")
    fun getAll(): List<Source>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg sources: Source)
}