package com.example.composeapp.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.composeapp.data.UserInfo
import com.example.composeapp.intent.UserIntent
import com.example.composeapp.viewModels.MainViewModel

@Composable
fun HomeScreen(mainViewModel: MainViewModel, navController: NavController) {
    val state by mainViewModel.state.collectAsState()

    var searchQuery by remember { mutableStateOf(mainViewModel.currentQuery) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {
                    searchQuery = it
                    mainViewModel.currentQuery = it
                },
                modifier = Modifier.weight(1f).padding(18.dp),
                label = { Text(text = "Enter name") },
            )
            Button(onClick = {
                if (searchQuery.isNotBlank()) {
                    mainViewModel.handleIntent(UserIntent.GetUsers(searchQuery))
                }
            }) {
                Text("Search")
            }
        }

        when {
            state.loading && state.users.isEmpty() -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            state.error != null -> {
                Text(text = "Error: ${state.error}", color = Color.Red)
            }

            else -> {
                UserList(users = state.users, navController = navController, loadMoreUsers = {
                    mainViewModel.handleIntent(UserIntent.LoadMoreUsers)
                })
            }
        }
    }
}

@Composable
fun UserList(users: List<UserInfo>, navController: NavController, loadMoreUsers: () -> Unit) {
    LazyColumn {
        itemsIndexed(users) { index, user ->
            if (index >= users.size - 5) {
                loadMoreUsers()
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .shadow(4.dp, RoundedCornerShape(8.dp))
                    .clickable {
                        navController.navigate("details/${user.name}")
                        {
                            navController.graph.startDestinationRoute?.let { route ->
                                popUpTo(route) {
                                    saveState = true
                                }
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
            ) {
                Row {
                    AsyncImage(
                        model = user.avatarUrl,
                        contentDescription = null,
                        modifier = Modifier.height(120.dp)
                    )
                    Text(
                        text = user.name,
                        modifier = Modifier
                            .padding(start = 20.dp)
                            .align(Alignment.CenterVertically)
                    )
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}