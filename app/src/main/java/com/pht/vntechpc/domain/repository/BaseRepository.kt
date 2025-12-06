package com.pht.vntechpc.domain.repository

import com.pht.vntechpc.data.remote.model.response.BaseResponse
import com.pht.vntechpc.utils.NetworkUtils
import retrofit2.Response
import java.io.IOException

abstract class BaseRepository {
    // Chỉ lấy ra data trả về
    protected suspend fun <T, R> apiCall(apiCall: suspend () -> Response<BaseResponse<T>>, transform: (T) -> R): Result<R> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    if (body.success) {
                        if (body.data != null) {
                            Result.success(transform(body.data))
                        } else {
                            Result.failure(Exception("Phản hồi từ server rỗng!"))
                        }
                    } else {
                        Result.failure(Exception(body.message))
                    }
                } else {
                    Result.failure(Exception("Lỗi không xác định!"))
                }
            } else {
                val errorMessage = NetworkUtils.parseErrorBody(response.errorBody())
                Result.failure(Exception(errorMessage))
            }
        } catch (_: IOException) {
            Result.failure(Exception("Không thể kết nối đến server"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Lấy nguyên data trả về
    protected suspend fun <T> apiCallRaw(apiCall: suspend () -> Response<BaseResponse<T>>): Result<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    if (body.success) {
                        if (body.data != null) {
                            Result.success(body.data)
                        } else {
                            Result.failure(Exception("Phản hồi từ server rỗng!"))
                        }
                    } else {
                        Result.failure(Exception(body.message))
                    }
                } else {
                    Result.failure(Exception("Lỗi không xác định!"))
                }
            } else {
                val errorMessage = NetworkUtils.parseErrorBody(response.errorBody())
                Result.failure(Exception(errorMessage))
            }
        } catch (_: IOException) {
            Result.failure(Exception("Không thể kết nối đến server"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Giữ nguyên response trả về
    protected suspend fun <Unit> apiCallNoData(apiCall: suspend () -> Response<BaseResponse<Unit>>): Result<BaseResponse<Unit>> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Lỗi không xác định!"))
                }
            } else {
                val errorMessage = NetworkUtils.parseErrorBody(response.errorBody())
                Result.failure(Exception(errorMessage))
            }
        } catch (_: IOException) {
            Result.failure(Exception("Không thể kết nối đến server"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}