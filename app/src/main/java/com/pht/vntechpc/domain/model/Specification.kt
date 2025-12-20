package com.pht.vntechpc.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Specification (
    val keyName: String,
    val value: String
): Parcelable