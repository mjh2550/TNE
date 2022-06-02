package com.solmi.biobrainexample.bio.data


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Bio_table")
class Bio (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "bio_id") var bioId : Long?,
    @ColumnInfo(name = "bio_emg_abs_avg") var mEmgAbsAvg : Float,
    @ColumnInfo(name = "bio_acc_abs_avg") var mAccAbsAvg : Float,
    @ColumnInfo(name = "bio_gyro_abs_avg") var mGyroAbsAvg : Float,
    @ColumnInfo(name = "bio_magneto_abs_avg") var mMagnetoAbsAvg : Float,
    @ColumnInfo(name = "reg_time") var regTime : String
){


    constructor(mEmgAbsAvg: Float,mAccAbsAvg: Float,mGyroAbsAvg: Float,mMagnetoAbsAvg: Float,regTime: String)
    :this(
        null,
        mEmgAbsAvg,
        mAccAbsAvg,
        mGyroAbsAvg,
        mMagnetoAbsAvg,
        regTime
    )
}