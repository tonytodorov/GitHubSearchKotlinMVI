package com.example.composeapp.repositories

import com.example.composeapp.data.GHResponse
import com.example.composeapp.data.UserInfo
import com.example.composeapp.utils.jsonInstance
import com.example.composeapp.data.UserInfoDetails
import com.example.composeapp.utils.GitHubApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import kotlin.coroutines.resume

class MainRepository @Inject constructor(private val httpClient: OkHttpClient) {

    suspend fun fetchUsers(query: String, page: Int, perPage: Int = 20): List<UserInfo> {
        return withContext(Dispatchers.IO) {
            suspendCancellableCoroutine { continuation ->

                val request = Request.Builder()
                    .url("${GitHubApi.BASE_URL}/search/users?q=$query&page=$page&per_page=$perPage")
                    .header("Authorization", "Bearer ${GitHubApi.TOKEN}")
                    .get()
                    .build()

                val response = httpClient.newCall(request).execute()

                val responseBody: String = response.body?.string()!!
                val ghResponse: GHResponse = jsonInstance.decodeFromString(responseBody)
                continuation.resume(ghResponse.items)
            }
        }
    }

    suspend fun fetchUserDetails(username: String): UserInfoDetails {
        return withContext(Dispatchers.IO) {
            val request = Request.Builder()
                .url("${GitHubApi.BASE_URL}/users/$username")
                .addHeader("Authorization", "Bearer ${GitHubApi.TOKEN}")
                .get()
                .build()

            val response = httpClient.newCall(request).execute()
            val responseBody = response.body?.string()!!

            jsonInstance.decodeFromString(responseBody)
        }
    }
}
