package com.exercise.music_exercise.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.exercise.music_exercise.data_models.List_HeaderDataModel
import com.exercise.music_exercise.data_models.List_ItemsDataModel
import com.exercise.music_exercise.data_models.PlayReportDataModel
import com.exercise.music_exercise.database.dao.CustomListDetailDao
import com.exercise.music_exercise.database.dao.MusicListDao
import com.exercise.music_exercise.database.dao.MusicListDetailDao
import com.exercise.music_exercise.database.dao.PlayReportDao

@Database(entities = arrayOf(List_HeaderDataModel::class, List_ItemsDataModel::class,
    PlayReportDataModel::class), version = 1, exportSchema = true)
open abstract class AppDataBase : RoomDatabase() {

    abstract fun musicListDao() : MusicListDao
    abstract fun musicListDetailDao() : MusicListDetailDao
    abstract fun playReportDao() : PlayReportDao
//    abstract fun customListDetailDao() : CustomListDetailDao


    companion object{
        var mContext: Context?= null
        var listener:RoomDatabase.Callback ?= null
        private val DB_NAME = "music_exercise-db"
        @Volatile private var instance:AppDataBase ?= null

        @JvmStatic
        fun getInstance(context: Context, callback:RoomDatabase.Callback):AppDataBase{
            listener = callback
            return instance ?: synchronized(this){
                instance ?: buildDataBase(context)
            }
        }

        @JvmStatic
        fun getInstance(context: Context):AppDataBase{
            listener = null
            return instance ?: synchronized(this){
                instance ?: buildDataBase(context)
            }
        }


//        val MIGRATION_1_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
////                var prefUtils : PreferenceUtils = PreferenceUtils.getInstance(mContext!!)
////                prefUtils.setBoolean("default_data", false)
//                var query : String = "CREATE TABLE 'play_exercise' (" +
//                        "'idx' LONG NOT NULL AUTO_INCREMENT PRIMARY_KEY, " +
//                        "'strDate text, " +
//                        "'healthListItemIdx' LONG)"
//                database.execSQL(query)
//            }
//        }

        open fun buildDataBase(context: Context):AppDataBase{
            mContext = context
            var builder = Room.databaseBuilder(context, AppDataBase::class.java, DB_NAME)
            builder.fallbackToDestructiveMigration()
                .allowMainThreadQueries()
//                .addMigrations(MIGRATION_1_2)

            if(listener != null )
                builder.addCallback(listener!!)

            var dataBase : AppDataBase = builder.build()
            return dataBase
        }
    }
}