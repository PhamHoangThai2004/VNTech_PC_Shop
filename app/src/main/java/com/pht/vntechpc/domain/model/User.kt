package com.pht.vntechpc.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int,
    val email: String,
    val fullName: String,
    val gender: String?,
    val avatar: String?,
    val dateOfBirth: String?
) : Parcelable