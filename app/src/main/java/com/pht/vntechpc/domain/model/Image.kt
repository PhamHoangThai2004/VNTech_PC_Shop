package com.pht.vntechpc.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Image (
    val imageUrl: String,
    val main: Boolean
): Parcelable