package com.solmi.biobrainexample.bio.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface BioDao {

    @Query("SELECT * FROM Bio_table ORDER BY bio_id ASC")
    fun getSelectBioList(): Flow<List<Bio>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(bio : Bio)

    @Query("DELETE FROM bio_table")
    suspend fun deleteAll()
}