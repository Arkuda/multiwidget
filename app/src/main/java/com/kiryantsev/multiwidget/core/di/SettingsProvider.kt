package com.kiryantsev.multiwidget.core.di

import com.kiryantsev.multiwidget.core.settings.SettingsEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class SettingsProvider {

    @Provides
    fun provideSettingsDto(): SettingsEntity? = SettingsEntity.load()
}