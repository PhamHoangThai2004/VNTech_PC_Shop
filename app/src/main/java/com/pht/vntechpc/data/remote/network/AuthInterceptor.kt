package com.pht.vntechpc.data.remote.network

import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation

class AuthInterceptor(private val tokenProvider: () -> String?) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val invocation = originalRequest.tag(Invocation::class.java)
        val noAuth = invocation?.method()?.getAnnotation(NoAuth::class.java) != null
        if (noAuth) {
            return chain.proceed(originalRequest)
        }

        val token = tokenProvider()

        val request = if (!token.isNullOrEmpty()) {
            originalRequest.newBuilder()
                .header("Authorization", "Bearer $token")
                .build()
        } else originalRequest

        return chain.proceed(request)
    }
}