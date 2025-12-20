package com.pht.vntechpc.di

import android.content.Context
import com.pht.vntechpc.data.remote.api.AddressApi
import com.pht.vntechpc.data.remote.api.AuthApi
import com.pht.vntechpc.data.remote.api.CartApi
import com.pht.vntechpc.data.remote.api.CategoryApi
import com.pht.vntechpc.data.remote.api.OrderApi
import com.pht.vntechpc.data.remote.api.ProductApi
import com.pht.vntechpc.data.remote.api.UserApi
import com.pht.vntechpc.data.remote.network.ApiClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideRetrofit(@ApplicationContext context: Context): Retrofit = ApiClient.create(context)

    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi = retrofit.create(AuthApi::class.java)

    @Provides
    @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)

    @Provides
    @Singleton
    fun provideCategoryApi(retrofit: Retrofit): CategoryApi =
        retrofit.create(CategoryApi::class.java)

    @Provides
    @Singleton
    fun provideProductApi(retrofit: Retrofit): ProductApi =
        retrofit.create(ProductApi::class.java)

    @Provides
    @Singleton
    fun provideCartApi(retrofit: Retrofit): CartApi =
        retrofit.create(CartApi::class.java)

    @Provides
    @Singleton
    fun provideAddressApi(retrofit: Retrofit): AddressApi =
        retrofit.create(AddressApi::class.java)

    @Provides
    @Singleton
    fun provideOrderApi(retrofit: Retrofit): OrderApi =
        retrofit.create(OrderApi::class.java)
}