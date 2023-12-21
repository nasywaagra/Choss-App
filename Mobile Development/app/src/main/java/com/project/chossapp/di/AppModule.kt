package com.project.chossapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.project.chossapp.data.local.ClothDao
import com.project.chossapp.data.local.ClothDatabase
import com.project.chossapp.data.remote.ApiService
import com.project.chossapp.data.repository.AuthRepository
import com.project.chossapp.data.repository.ClothRepository
import com.project.chossapp.util.Dimen.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(
                        HttpLoggingInterceptor().setLevel(
                            HttpLoggingInterceptor.Level.BODY
                        )
                    )
                    .build()
            )
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        apiService: ApiService,
        @ApplicationContext context: Context
    ): AuthRepository = AuthRepository(apiService, context)

    @Provides
    @Singleton
    fun provideDatabase(
        application: Application
    ): ClothDatabase {
        return Room.databaseBuilder(
            context = application,
            klass = ClothDatabase::class.java,
            name = "cloth_db"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideClothDao(
        clothDatabase: ClothDatabase
    ): ClothDao = clothDatabase.clothDao

    @Provides
    @Singleton
    fun provideClothRepository(
        clothDao: ClothDao
    ): ClothRepository = ClothRepository(clothDao)
}