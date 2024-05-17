package com.naren.sarvajna.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tripadis")
data class Tripadi (@ColumnInfo(name = "favorite") var favorite : Boolean? = null, @ColumnInfo(name = "tripadi") val tripadi : String, @ColumnInfo(name = "_id") @PrimaryKey(autoGenerate = true) val _id : Int) {

    override fun toString(): String {
        return tripadi
    }

}