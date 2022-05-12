package com.hyperone.assignment.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Source entity
 *
 * @author Abdelaziz Daoud
 * @since 11/05/2022

 * @property uid Int
 * @property type String?
 * @property url String?
 * @property name String?
 * @constructor
 */
@Entity
data class Source(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "name") val name: String?
)