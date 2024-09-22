package com.example.composeapp.compose

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.composeapp.destination.MainDestinations
import com.example.composeapp.intent.UserIntent
import com.example.composeapp.viewModels.MainViewModel

@Composable
fun DetailsScreen(username: String, mainViewModel: MainViewModel, navController: NavController) {
    val context = LocalContext.current
    val usersDetails by mainViewModel.selectedUserDetails.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(username) {
        mainViewModel.handleIntent(UserIntent.GetUserDetails(username))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        usersDetails?.let { user ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                AsyncImage(
                    model = user.avatarUrl,
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .height(300.dp)
                        .fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Login: ${user.login}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "ID: ${user.id}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Node ID: ${user.nodeId}")
            Spacer(modifier = Modifier.height(8.dp))

            listOf(
                "Avatar URL" to user.avatarUrl,
                "URL" to user.url,
                "HTML URL" to user.htmlUrl,
                "Followers URL" to user.followersUrl,
                "Following URL" to user.followingUrl,
                "Gists URL" to user.gistsUrl,
                "Starred URL" to user.starredUrl,
                "Subscriptions URL" to user.subscriptionsUrl,
                "Organizations URL" to user.organizationsUrl,
                "Repos URL" to user.reposUrl,
                "Events URL" to user.eventsUrl,
                "Received Events URL" to user.receivedEventsUrl
            ).forEach { (label, url) ->
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = label,
                    style = TextStyle(color = Color.Blue),
                    modifier = Modifier.clickable {
                        openUrl(context, url)
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Type: ${user.type}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Site Admin: ${if (user.siteAdmin) "Yes" else "No"}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Name: ${user.name ?: "N/A"}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Company: ${user.company ?: "N/A"}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Blog: ${user.blog}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Location: ${user.location ?: "N/A"}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Email: ${user.email ?: "N/A"}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Hireable: ${user.hireable?.toString() ?: "N/A"}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Bio: ${user.bio ?: "N/A"}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Twitter Username: ${user.twitterUsername ?: "N/A"}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Public Repos: ${user.publicRepos}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Public Gists: ${user.publicGists}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Followers: ${user.followers}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Following: ${user.following}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Created At: ${user.createdAt}")
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Updated At: ${user.updatedAt}")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                navController.navigate(MainDestinations.HOME_ROUTE) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = false
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }) {
                Text("Back")
            }
        }
    }
}

private fun openUrl(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}
