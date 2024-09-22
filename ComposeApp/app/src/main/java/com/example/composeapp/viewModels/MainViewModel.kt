package com.example.composeapp.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.composeapp.repositories.MainRepository
import com.example.composeapp.data.UserInfo
import com.example.composeapp.data.UserInfoDetails
import com.example.composeapp.data.UserViewState
import com.example.composeapp.intent.UserIntent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: MainRepository) : ViewModel() {

    private var _state = MutableStateFlow(UserViewState())
    val state: StateFlow<UserViewState> = _state

    private val _selectedUserDetails = MutableStateFlow<UserInfoDetails?>(null)
    val selectedUserDetails: StateFlow<UserInfoDetails?> = _selectedUserDetails

    private var allUsers: MutableList<UserInfo> = mutableListOf()
    private var currentPage: Int = 1
    var currentQuery: String = ""

    fun handleIntent(intent: UserIntent) {
        viewModelScope.launch {
            when (intent) {
                is UserIntent.GetUsers -> getUsers(intent.query)
                is UserIntent.GetUserDetails -> getUserDetails(intent.username)
                is UserIntent.LoadMoreUsers -> loadMoreUsers()
            }
        }
    }

    private fun getUsers(query: String) {
        viewModelScope.launch {
            try {
                updateState(loading = true)
                currentQuery = query
                currentPage = 1
                val users = repository.fetchUsers(query, currentPage, 20)
                allUsers = users.toMutableList()
                updateState(users = allUsers, loading = false)
            } catch (e: Exception) {
                errorMessage(e)
            }
        }
    }

    private fun getUserDetails(username: String) {
        viewModelScope.launch {
            try {
                updateState(loading = true)
                val userDetails = repository.fetchUserDetails(username)
                _selectedUserDetails.value = userDetails
                updateState(loading = false)
            } catch (e: Exception) {
                errorMessage(e)
            }
        }
    }

    private fun loadMoreUsers() {
        viewModelScope.launch {
            try {
                updateState(loading = true)
                currentPage += 1
                val users = repository.fetchUsers(currentQuery, currentPage, 20)
                allUsers.addAll(users)
                updateState(users = allUsers, loading = false)
            } catch (e: Exception) {
                errorMessage(e)
            }
        }
    }

    private fun updateState(users: List<UserInfo>? = null, loading: Boolean = false, error: String? = null) {
        _state.value = UserViewState(
            users = users ?: _state.value.users,
            loading = loading,
            error = error
        )
    }

    private fun errorMessage(e: Exception) {
        Log.d("Error", "Error: ${e.message}")
    }
}