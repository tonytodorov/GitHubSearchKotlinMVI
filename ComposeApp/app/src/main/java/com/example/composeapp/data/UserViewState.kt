package com.example.composeapp.data

data class UserViewState (
    val loading: Boolean = false,
    val error: String? = null,
    val users: List<UserInfo> = emptyList(),
)