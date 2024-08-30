package com.example.expensetrackerapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.expensetrackerapp.components.AppAlertDialog
import com.example.expensetrackerapp.model.PasswordChangeResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun ProfileScreen(
    username: String,
    email: String,
    changePassword: suspend (String, String) -> PasswordChangeResponse,
    logoutOnClick: () -> Unit,
    deleteAccount: suspend () -> Boolean
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordChangeSuccess by remember { mutableStateOf(false) }
    var passwordChangeText by remember { mutableStateOf("") }
    var isChangePasswordLoading by remember { mutableStateOf(false) }
    var isDeleteAccountLoading by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogText by remember { mutableStateOf("") }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogConfirm by remember { mutableStateOf({}) }
    var deleteAccountConfirm by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row {
                Text(
                    text = "Username: ",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = username,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Text(
                    text = "Email: ",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = email,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(3f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = "Change Password",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = currentPassword,
                onValueChange = { currentPassword = it },
                label = { Text("Current Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(16.dp),
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = { Text("New Password") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(16.dp),
                visualTransformation = PasswordVisualTransformation(),
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm New Password") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                shape = RoundedCornerShape(16.dp),
                visualTransformation = PasswordVisualTransformation(),
            )

            Spacer(modifier = Modifier.height(16.dp))

            if(isChangePasswordLoading) {
                CircularProgressIndicator()
            }
            else {
                Button(
                    onClick = {
                        dialogTitle = "Confirm Change Password"
                        dialogText = "Are you sure you want to change you password?"
                        dialogConfirm = {
                            if (newPassword == confirmPassword && newPassword.isNotEmpty()) {
                                isChangePasswordLoading = true
                                CoroutineScope(Dispatchers.IO).launch {
                                    val response = changePassword(currentPassword, newPassword)
                                    isChangePasswordLoading = false
                                    println("Profile error: ${response.message}")
                                    passwordChangeSuccess = response.success
                                    passwordChangeText = response.message
                                }
                            } else {
                                passwordChangeSuccess = false
                                passwordChangeText = "Passwords do not match or are empty"
                            }
                        }
                        showDialog = true
                    },
                ) {
                    Text("Change Password")
                }
            }


            if (passwordChangeText.isNotEmpty()) {
                Text(
                    text = passwordChangeText,
                    color = if(passwordChangeSuccess) Color.Green else Color.Red,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .align(Alignment.End)
                .weight(1f),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Row {
                if(isDeleteAccountLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                else {
                    Button(
                        onClick = {
                            dialogTitle = "Confirm Delete Account"
                            dialogText = "Are you sure you want to delete your account?"
                            dialogConfirm = {
                                isDeleteAccountLoading = true
                                CoroutineScope(Dispatchers.IO).launch {
                                    deleteAccountConfirm = deleteAccount()
                                    isDeleteAccountLoading = false
                                    withContext(Dispatchers.Main) {
                                        if(deleteAccountConfirm) {
                                            logoutOnClick()
                                        }
                                    }
                                }
                            }
                            showDialog = true
                        },
                        colors = ButtonDefaults.buttonColors(
                            Color.Red
                        )
                    ) {
                        Text(text = "Delete account")
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(onClick =  {
                    dialogTitle = "Confirm Logout"
                    dialogText = "Are you sure you want to logout?"
                    dialogConfirm = logoutOnClick
                    showDialog = true
                }) {
                    Text(text = "Logout")
                }

            }
        }

        if(showDialog) {
            AppAlertDialog(
                title = dialogTitle,
                text = dialogText,
                onClickConfirm = dialogConfirm,
                onClickDismiss = {showDialog = false}
            )
        }

        if(passwordChangeSuccess) {
            currentPassword = ""
            newPassword = ""
            confirmPassword = ""
        }
    }
}

