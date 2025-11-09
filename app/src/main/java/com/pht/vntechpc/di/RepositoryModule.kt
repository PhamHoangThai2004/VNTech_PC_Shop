package com.pht.vntechpc.di

import com.pht.vntechpc.data.repository.AuthRepositoryImpl
import com.pht.vntechpc.data.repository.CategoryRepositoryImpl
import com.pht.vntechpc.data.repository.ProductRepositoryImpl
import com.pht.vntechpc.data.repository.UserRepositoryImpl
import com.pht.vntechpc.domain.repository.AuthRepository
import com.pht.vntechpc.domain.repository.CategoryRepository
import com.pht.vntechpc.domain.repository.ProductRepository
import com.pht.vntechpc.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository

    @Binds
    @Singleton
    abstract fun bindCategoryRepository(categoryRepositoryImpl: CategoryRepositoryImpl): CategoryRepository

    @Binds
    @Singleton
    abstract fun bindProductRepository(productRepositoryImpl: ProductRepositoryImpl): ProductRepository
}