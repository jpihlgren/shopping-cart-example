package com.example.simpleshopping.di

import android.content.Context
import com.example.simpleshopping.data.ProductRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProductModule {

    @Provides
    @Singleton
    fun provideProductRepository(@ApplicationContext context: Context): ProductRepository {
        return ProductRepository(context)
    }
}