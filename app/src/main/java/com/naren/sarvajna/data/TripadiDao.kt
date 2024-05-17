package com.naren.sarvajna.data

import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TripadiDao {

    @Query("SELECT * from tripadis where tripadi LIKE :query ORDER BY tripadi ASC")
    fun query(query : String) : List<Tripadi>

    @Query("SELECT * from tripadis where tripadi LIKE :query AND favorite = :favorite ORDER BY tripadi ASC")
    fun query(query : String, favorite : Int) : List<Tripadi>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(tripadi: Tripadi)
}