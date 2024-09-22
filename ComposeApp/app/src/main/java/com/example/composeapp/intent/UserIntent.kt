package com.example.composeapp.intent

sealed class UserIntent {
    data class GetUsers(val query: String) : UserIntent()
    data class GetUserDetails(val username: String): UserIntent()
    data object LoadMoreUsers: UserIntent()
}