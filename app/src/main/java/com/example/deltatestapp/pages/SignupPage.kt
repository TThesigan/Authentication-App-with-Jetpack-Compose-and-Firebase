package com.example.deltatestapp.pages

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.deltatestapp.AuthViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import com.example.deltatestapp.AuthState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupPage(
    modifier: Modifier = Modifier,
    navController: NavController,
    authViewModel: AuthViewModel
) {
    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Male") } // default value
    var expanded by remember { mutableStateOf(false) }
    var selectedCity by remember { mutableStateOf("Choose your city") }
    var chkBoxTerms by remember { mutableStateOf(false) }

    val cities = listOf("Jaffna", "Kandy", "Colombo", "Galle", "Trinco", "Nuwara Eliya")
    val authState = authViewModel.authState.observeAsState()
    val context = LocalContext.current

    LaunchedEffect(authState.value) {
        when (authState.value) {
            is AuthState.Authenticated -> navController.navigate("home")
            {
                popUpTo("signup") { inclusive = true } // Clear back stack
            }

            is AuthState.Error -> Toast.makeText(
                context,
                (authState.value as AuthState.Error).message, Toast.LENGTH_SHORT
            ).show()

            else -> Unit
        }
    }

    // Determine if the button should be enabled
    val isButtonEnabled = userName.isNotEmpty() && email.isNotEmpty() && password.length >= 6 &&
            selectedCity != "Choose your city" && chkBoxTerms

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Sign up", fontSize = 32.sp)
        Spacer(modifier = Modifier.height(16.dp))

        // Username Text field
        OutlinedTextField(
            value = userName,
            onValueChange = { userName = it },
            label = {
                Text(text = "User Name")
            })
        Spacer(modifier = Modifier.height(8.dp))

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
            label = { Text(text = "Email") }
        )
        if (emailError.isNotEmpty()) {
            Text(
                text = emailError,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 4.dp
                ) // Add padding to align with the text field
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Password text field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation()
        )
        if (passwordError.isNotEmpty()) {
            Text(
                text = passwordError,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(
                    start = 16.dp,
                    top = 4.dp
                ) // Add padding to align with the text field
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Gender Selection
        Text(text = "Select Gender")
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(
                selected = gender == "Male",
                onClick = { gender = "Male" }
            )
            Text(text = "Male", modifier = Modifier.padding(end = 16.dp))

            RadioButton(
                selected = gender == "Female",
                onClick = { gender = "Female" }
            )
            Text(text = "Female")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Dropdown for City selection
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedCity,
                onValueChange = { },
                label = { Text(text = "City") },
                readOnly = true,
                trailingIcon = {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown arrow")
                },
                modifier = Modifier
                    .menuAnchor()
                    .align(Alignment.CenterHorizontally)
                    .clickable { expanded = true } // Ensure dropdown opens on click
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                cities.forEach { city ->
                    DropdownMenuItem(
                        text = { Text(text = city) },
                        onClick = {
                            selectedCity = city
                            expanded = false
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Checkbox for terms and conditions
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Checkbox(
                checked = chkBoxTerms,
                onCheckedChange = { chkBoxTerms = it }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "I agree to the terms and conditions",
                modifier = Modifier.clickable {
                    chkBoxTerms = !chkBoxTerms
                } // Allows clicking the text to toggle the checkbox
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Submit button for sign up
        Button(
            onClick = {
                if (password.length < 6) {
                    passwordError = "Password must be at least 6 characters long"
                } else {
                    passwordError = ""
                    authViewModel.signup(email, password, userName)
                }
            },
            enabled = isButtonEnabled && authState.value != AuthState.Loading
        )
        {
            Text(text = "Create account")
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Link for sign in
        TextButton(onClick = {
            navController.navigate("signin")
        }) {
            Text(text = "Already have an account, Sign in")
        }

    }
}