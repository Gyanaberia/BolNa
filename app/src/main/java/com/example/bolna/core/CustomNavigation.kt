package com.example.bolna.core

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bolna.auth.viewModel.LoginViewModel
import com.example.bolna.auth.viewModel.RegisterViewModel
import com.example.bolna.auth.view.LoginScreen
import com.example.bolna.auth.view.LoginUiActions
import com.example.bolna.auth.view.RegisterScreen
import com.example.bolna.auth.view.RegisterUiActions
import com.example.bolna.auth.util.rememberGoogleSignIn

@Composable
fun CustomNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            LoginRoute(
                onNavigateToRegister = { navController.navigate("register") }
            )
        }

        composable("register") {
            // Replace with your RegisterScreen when it's ready
            RegisterRoute(
                onNavigateToLogin = {navController.navigate("login")}
            )
        }
    }
}


@Composable
fun LoginRoute(
    onNavigateToRegister: () -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val onGoogleClick = rememberGoogleSignIn(
        onToken = { token ->
            Log.d("GoogleAuth", "Got token, starts with: ${token.take(15)}")
            viewModel.loginWithGoogle(token)
        },
        onError = viewModel::showError
    )
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
    viewModel: RegisterViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val onGoogleClick = rememberGoogleSignIn(
        onToken = { token ->
            Log.d("GoogleAuth", "Got token, starts with: ${token.take(15)}")
            viewModel.loginWithGoogle(token)
        },
        onError = viewModel::showError
    )
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