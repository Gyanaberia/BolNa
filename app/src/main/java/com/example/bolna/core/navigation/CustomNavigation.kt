package com.example.bolna.core.navigation

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bolna.auth.viewModel.LoginViewModel
import com.example.bolna.auth.viewModel.RegisterViewModel
import com.example.bolna.auth.view.LoginScreen
import com.example.bolna.auth.view.LoginUiActions
import com.example.bolna.auth.view.RegisterScreen
import com.example.bolna.auth.view.RegisterUiActions
import com.example.bolna.auth.google.rememberGoogleSignIn
import com.example.bolna.chats.ui.home.ChatHomeScreen
import com.example.bolna.chats.ui.home.HomeUiActions
import com.example.bolna.chats.ui.home.HomeViewModel

@Composable
fun CustomNavigation(
    appViewModel: AppViewModel = hiltViewModel()
) {
    val startDestination by appViewModel.startDestination.collectAsState()
    val navController = rememberNavController()
    // Still reading the token: show a spinner
    if (startDestination == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    NavHost(navController = navController, startDestination = Routes.HOME) {

        composable(Routes.LOGIN) {
            LoginRoute(
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) },
                onAuthenticated = { navController.goToHomeClearingBackStack() }
            )
        }

        composable(Routes.REGISTER) {
            // Replace with your RegisterScreen when it's ready
            RegisterRoute(
                onNavigateToLogin = {navController.navigate(Routes.LOGIN)},
                onAuthenticated = {navController.goToHomeClearingBackStack()}
            )
        }

        composable(Routes.HOME){
            HomeRoute(
                onOpenRoom = { /* TODO: chat screen */ },
                onNewDMChat = { /* TODO: user search screen */ },
                onNewGroupChat = {/* TODO: multi user search screen */},
                onOpenProfile = { /* TODO */ },
                onOpenSettings = { /* TODO */ },
                onLoggedOut = {
                    navController.navigate(Routes.LOGIN) {
                        //It removes everything from the back stack before showing the new screen.
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }
    }
}


@Composable
fun LoginRoute(
    onNavigateToRegister: () -> Unit,
    onAuthenticated: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val onGoogleClick = rememberGoogleSignIn(
        onToken = { token ->
            Log.d("GoogleAuth", "Got token, starts with: ${token.take(15)}")
            viewModel.loginWithGoogle(token)
        },
        onError = viewModel::showError
    )

    LaunchedEffect(state.isAuthenticated) {
        if (state.isAuthenticated) onAuthenticated()
    }
    LoginScreen(
        state = state,
        actions = LoginUiActions(
            onUsernameChange = viewModel::onUsernameChange,
            onPasswordChange = viewModel::onPasswordChange,
            onLoginClick = viewModel::login,
            onGoogleClick = onGoogleClick,
            onRegisterClick = onNavigateToRegister

        )
    )
}

@Composable
fun RegisterRoute(
    onNavigateToLogin: () -> Unit,
    onAuthenticated: () -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val onGoogleClick = rememberGoogleSignIn(
        onToken = { token ->
            Log.d("GoogleAuth", "Got token, starts with: ${token.take(15)}")
            viewModel.loginWithGoogle(token)
        },
        onError = viewModel::showError
    )
    LaunchedEffect(state.isAuthenticated) {
        if (state.isAuthenticated) onAuthenticated()
    }
    RegisterScreen(
        state = state,
        actions = RegisterUiActions(
            onUsernameChange = viewModel::onUsernameChange,
            onPasswordChange = viewModel::onPasswordChange,
            onEmailChange = viewModel::onEmailChange,
            onMobileNumberChange = viewModel::onMobileChange,
            onLoginClick = onNavigateToLogin,
            onGoogleClick = onGoogleClick,
            onRegisterClick = viewModel::register
        )
    )
}


@Composable
fun HomeRoute(
    onOpenRoom: (String) -> Unit,
    onOpenProfile: () -> Unit,
    onNewDMChat:() -> Unit,
    onNewGroupChat:() -> Unit,
    onOpenSettings: () -> Unit,
    onLoggedOut: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(state.isLoggedOut) {
        if (state.isLoggedOut) onLoggedOut()
    }

    ChatHomeScreen(
        state = state,
        actions = HomeUiActions(
            onSearchChange = viewModel::onSearchChange,
            onRoomClick = onOpenRoom,
            onNewChatClick = viewModel::onNewChatClick,
            onNewDMClick = onNewDMChat,
            onNewGroupClick = onNewGroupChat,
            onProfileClick = onOpenProfile,

            onSettingsClick = onOpenSettings,
            onLogOutClick = viewModel::logOut
        )
    )
}

private fun NavController.goToHomeClearingBackStack() {
    Log.i("Home","Navigating to Home")
    navigate(Routes.HOME) {
        popUpTo(graph.id) { inclusive = true }
    }
}
