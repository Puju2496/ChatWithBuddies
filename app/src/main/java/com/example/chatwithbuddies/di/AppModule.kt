package com.example.chatwithbuddies.di

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.example.chatwithbuddies.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context
    ): SharedPreferences = context.getSharedPreferences(
        context.getString(R.string.app_name),
        AppCompatActivity.MODE_PRIVATE
    )
}