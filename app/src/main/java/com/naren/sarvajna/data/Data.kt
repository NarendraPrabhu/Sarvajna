package com.naren.sarvajna.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "tripadis")
data class Tripadi (@ColumnInfo(name = "_id") @PrimaryKey(autoGenerate = true) val _id : Int, @ColumnInfo(name = "tripadi") val tripadi : String, @ColumnInfo(name = "favorite") var favorite : Boolean) {

    override fun toString(): String {
        return tripadi
    }

}