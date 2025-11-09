package com.pht.vntechpc.utils

import com.google.gson.Gson
import com.pht.vntechpc.data.remote.model.response.BaseResponse
import okhttp3.ResponseBody

object NetworkUtils {
    fun parseErrorBody(errorBody: ResponseBody?): String {
        return try {
            val errorJson = errorBody?.string()
            val errorResponse = Gson().fromJson(errorJson, BaseResponse::class.java)
            errorResponse?.message ?: "Đã xảy ra lỗi"
        } catch (e: Exception) {
            e.message ?: "Đã xảy ra lỗi"
        }
    }
}