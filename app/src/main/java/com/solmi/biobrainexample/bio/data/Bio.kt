package com.solmi.biobrainexample.bio.data


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.solmi.biobrainexample.common.CircularQueue


@Entity(tableName = "Bio_table")
class Bio (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bio_id") var bioId : Long?,
    @ColumnInfo(name = "bio_data") var bioData : String
){

    constructor(bioData: String):this(
        null,
        bioData
    )
}