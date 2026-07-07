package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class BraviaDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var INSTANCE: BraviaDatabase? = null

        fun getDatabase(context: Context): BraviaDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BraviaDatabase::class.java,
                    "bravia_desktop_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
