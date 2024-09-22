package com.example.composeapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    @SerialName("login") val name: String,
    @SerialName("avatar_url") val avatarUrl: String,
)
