package com.juandgaines.seedqrvalidator.core.domain

data class Seed(
    val seed:String,
    val expiresAt:String,
    val type:SeedType = SeedType.GENERATED
)

enum class SeedType{
    SCANNED,
    GENERATED;


    fun toInt():Int{
        return when(this){
            SCANNED -> SCANNED.ordinal
            GENERATED -> GENERATED.ordinal
        }
    }
    companion object{
        fun fromInt(value:Int):SeedType{
            return when(value){
                SCANNED.ordinal -> SCANNED
                GENERATED.ordinal -> GENERATED
                else -> throw IllegalArgumentException("Invalid SeedType value: $value")
            }
        }
    }

}