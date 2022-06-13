package com.solmi.biobrainexample.bio.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.solmi.biobrainexample.common.CircularQueue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@Database(entities = [Bio::class] , version = 2)
abstract class BioRoomDatabase : RoomDatabase() {

    abstract fun bioDao() : BioDao

    companion object{

        @Volatile
        private var INSTANCE : BioRoomDatabase? = null

        fun getDatabase(
            context:Context
            ,scope: CoroutineScope
        ): BioRoomDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BioRoomDatabase::class.java,
                    "bio_database"
                ).fallbackToDestructiveMigration()
                    .addCallback(BioRoomDatabaseCallBack(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }


        private class BioRoomDatabaseCallBack(private val scope: CoroutineScope):RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let {
                    bioRoomDatabase ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(bioRoomDatabase.bioDao())
                    }
                }
            }


        }


        suspend fun populateDatabase(bioDao: BioDao){
//            bioDao.deleteAll()
//            val bioDataQueue = CircularQueue()
//            for(i in 0..10){
//                bioDataQueue.insert(0.12522525f)
//            }
//
//            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//            val format_time: String = format.format(System.currentTimeMillis())
////            var bio = Bio(0,1.0F,1.0F,1.0F,1.0F,format_time)
//            var bio = Bio(0,bioDataQueue,format_time)
//            bioDao.insert(bio)
        }
    }
}