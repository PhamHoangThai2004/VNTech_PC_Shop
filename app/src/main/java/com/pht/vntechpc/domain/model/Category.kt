package com.pht.vntechpc.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(
    val id: Int,
    val categoryName: String,
    val createdAt: String?,
    val updatedAt: String?
) : Parcelable