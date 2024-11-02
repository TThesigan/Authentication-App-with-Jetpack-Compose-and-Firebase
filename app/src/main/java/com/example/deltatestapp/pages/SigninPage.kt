package com.example.deltatestapp.pages

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.deltatestapp.AuthState
import com.example.deltatestapp.AuthViewModel

@Composable
fun SigninPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var signinError by remember { mutableStateOf("") }

    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> navController.navigate("home")
            is AuthState.Error -> {
                val errorMessage = "Invalid email or password"
                if (errorMessage.contains("The supplied auth credential is incorrect")) {
                    signinError = errorMessage
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }

            else -> Unit
        }
    }

    val isButtonEnabled = email.isNotEmpty() && password.isNotEmpty() && emailError.isEmpty()

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Sign in", fontSize = 32.sp, modifier = Modifier.testTag("signinTitle"))
        Spacer(modifier = Modifier.height(16.dp))

        // Email Text field
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
                // Validate email format
                emailError = if (!it.contains("@") || !it.contains(".")) {
                    "Email format should be like abc@mail.com"
                } else {
                    ""
                }
            },
            label = { Text(text = "Email") },
            modifier = Modifier.testTag("emailEditText")
        )
        if (emailError.isNotEmpty()) {
            Text(
                text = emailError,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        top = 4.dp
                    )
                    .testTag("emailErrorMsg")
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.testTag("passwordEditText")
        )
        if (passwordError.isNotEmpty()) {
            Text(
                text = passwordError,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(
                        start = 16.dp,
                        top = 4.dp
                    )
                    .testTag("passwordErrorMsg")
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = signinError,
            color = Color.Red,
            fontSize = 12.sp,
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    top = 4.dp
                )
                .testTag("signinErrorMsg")
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                authViewModel.signin(email, password)
            },
            enabled = isButtonEnabled && authState.value != AuthState.Loading,
            modifier = Modifier
                .testTag("signinBtn")
                .semantics {
                    contentDescription = when {
                        !isButtonEnabled -> "button_disabled"
                        authState.value == AuthState.Loading -> "button_disabled"
                        else -> "button_enabled"
                    }
                }
        )
        {
            Text(text = "Sign in")
        }
        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = {
            navController.navigate("signup")
        }, modifier = Modifier.testTag("signupLink")) {
            Text(text = "Don't have, Create an account")
        }
    }
}