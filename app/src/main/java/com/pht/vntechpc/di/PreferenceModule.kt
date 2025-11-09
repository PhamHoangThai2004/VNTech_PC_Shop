package com.pht.vntechpc.di

import android.content.Context
import com.pht.vntechpc.data.local.UserPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PreferenceModule {
    @Provides
    @Singleton
    fun providerUserPreferences(@ApplicationContext context: Context): UserPreferences =
        UserPreferences(context)
}