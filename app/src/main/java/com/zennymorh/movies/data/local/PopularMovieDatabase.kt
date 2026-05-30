package com.zennymorh.movies.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zennymorh.movies.data.model.PopularMovieEntity
import com.zennymorh.movies.data.model.RemoteKey

@Database(
    entities = [PopularMovieEntity::class, RemoteKey::class],
    version = 3,
    exportSchema = true
)
abstract class PopularMovieDatabase : RoomDatabase() {
    abstract fun movieDao(): PopularMovieDao
    abstract fun remoteKeyDao(): RemoteKeyDao
}
