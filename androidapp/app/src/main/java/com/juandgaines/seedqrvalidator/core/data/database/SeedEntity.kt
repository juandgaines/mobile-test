package com.juandgaines.seedqrvalidator.core.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "seeds")
data class SeedEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Long? = null,
    val seed:String,
    val type:Int,
    val expirationTime:String
)