import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.composeapp.compose.DetailsScreen
import com.example.composeapp.destination.GithubNavController
import com.example.composeapp.destination.MainDestinations
import com.example.composeapp.compose.HomeScreen
import com.example.composeapp.intent.UserIntent
import com.example.composeapp.viewModels.MainViewModel

@Composable
fun NavGraph(mainViewModel: MainViewModel, navController: GithubNavController) {
    NavHost(
        navController = navController.navController,
        startDestination = MainDestinations.HOME_ROUTE
    ) {
        composable(MainDestinations.HOME_ROUTE) {
            HomeScreen(mainViewModel = mainViewModel, navController = navController.navController)
        }
        composable(
            route = MainDestinations.USER_DETAIL_ROUTE,
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: return@composable
            mainViewModel.handleIntent(UserIntent.GetUserDetails(username))
            DetailsScreen(username = username, mainViewModel = mainViewModel, navController = navController.navController)
        }
    }
}
