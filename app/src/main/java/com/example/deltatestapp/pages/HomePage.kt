package com.example.deltatestapp.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.deltatestapp.AuthState
import com.example.deltatestapp.AuthViewModel

@Composable
fun HomePage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    val authState = authViewModel.authState.observeAsState()
    val username = authViewModel.currentUsername

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Unauthenticated -> navController.navigate("signin")
            else -> Unit
        }
    }
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Welcome ${username ?: "User"}",
            fontSize = 32.sp,
            modifier = Modifier.testTag("welcomeNote")
        )

        TextButton(onClick = {
            authViewModel.signout()
        }, modifier = Modifier.testTag("signoutLink")) {
            Text(text = "Sign out")
        }
    }
}