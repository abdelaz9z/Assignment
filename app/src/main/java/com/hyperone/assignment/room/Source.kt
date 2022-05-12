package com.hyperone.assignment.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Source(
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "type") val type: String?,
    @ColumnInfo(name = "url") val url: String?,
    @ColumnInfo(name = "name") val name: String?
)