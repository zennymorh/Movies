package com.zennymorh.movies.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.zennymorh.movies.data.model.PopularMovieEntity

@Database(entities = [PopularMovieEntity::class], version = 1, exportSchema = false)
abstract class PopularMovieDatabase : RoomDatabase() {
    abstract fun movieDao(): PopularMovieDao
}