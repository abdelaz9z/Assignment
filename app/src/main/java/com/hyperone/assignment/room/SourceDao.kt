package com.hyperone.assignment.room

import androidx.room.*

@Dao
interface SourceDao {
    @Query("SELECT * FROM source")
    fun getAll(): List<Source>

    @Query("SELECT * FROM source WHERE uid IN (:type)")
    fun loadAllByIds(type: IntArray): List<Source>

    @Query("SELECT * FROM source WHERE name LIKE :name AND " + "type LIKE :type LIMIT 1")
    fun findByName(name: String, type: String): Source

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg sources: Source)

    @Delete
    fun delete(source: Source)
}