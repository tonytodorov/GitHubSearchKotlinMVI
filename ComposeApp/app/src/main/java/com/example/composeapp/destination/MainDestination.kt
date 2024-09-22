package com.example.composeapp.destination

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

object MainDestinations {
    const val HOME_ROUTE = "home"
    const val USER_DETAIL_ROUTE = "details/{username}"
}

@Composable
fun rememberGithubNavController(
    navController: NavHostController = rememberNavController()
): GithubNavController = remember(navController) {
    GithubNavController(navController)
}

@Stable
class GithubNavController(val navController: NavHostController)

