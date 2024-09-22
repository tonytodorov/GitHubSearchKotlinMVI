package com.example.composeapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GHResponse (
    @SerialName("total_count") val totalCount: Int,
    @SerialName("items") val items: List<UserInfo>
)