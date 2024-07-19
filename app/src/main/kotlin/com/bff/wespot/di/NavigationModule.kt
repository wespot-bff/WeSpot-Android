package com.bff.wespot.di

import com.bff.wespot.NavigatorImpl
import com.wespot.navigation.Navigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {
    @Binds
    @Singleton
    fun bindsNavigator(navigator: NavigatorImpl): Navigator
}
