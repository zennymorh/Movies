package com.zennymorh.movies.di.module

import android.content.Context
import androidx.room.Room
import com.zennymorh.movies.BuildConfig
import com.zennymorh.movies.api.ApiService
import com.zennymorh.movies.api.AuthInterceptor
import com.zennymorh.movies.data.PopularMoviesRepository
import com.zennymorh.movies.data.PopularMoviesRepositoryImpl
import com.zennymorh.movies.data.datasource.PopularMoviesDataSource
import com.zennymorh.movies.data.datasource.local.LocalPopularMoviesDataSource
import com.zennymorh.movies.data.datasource.remote.RemotePopularMoviesDataSourceImpl
import com.zennymorh.movies.roomdb.PopularMovieDao
import com.zennymorh.movies.roomdb.PopularMovieDatabase
import com.zennymorh.movies.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {
    @Provides
    fun provideBaseUrl(): String {
        return BASE_URL
    }

    @Singleton
    @Provides
    fun provideHttpClient(): OkHttpClient {
        return OkHttpClient
            .Builder()
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(provideAuthInterceptor())
            .build()
    }

    @Provides
    fun provideAuthInterceptor(): AuthInterceptor {
        return AuthInterceptor(BuildConfig.API_KEY)
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory =
        GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRetrofit(
        baseUrl: String,
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    fun provideRemotePopularMoviesDataSource(
        movieApi: ApiService,
    ): PopularMoviesDataSource {
        return RemotePopularMoviesDataSourceImpl(movieApi)
    }

    @Provides
    fun providePopularMoviesRepository(
        popularMovieDao: PopularMovieDao,
        movieApi: ApiService,
        popularMovieDatabase: PopularMovieDatabase,
    ): PopularMoviesRepository {
        return PopularMoviesRepositoryImpl(popularMovieDao, movieApi, popularMovieDatabase)
    }

    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): PopularMovieDatabase {
        return Room.databaseBuilder(
            context,
            PopularMovieDatabase::class.java,
            "movie_database"
        ).fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieDao(movieDatabase: PopularMovieDatabase): PopularMovieDao {
        return movieDatabase.movieDao()
    }
}
