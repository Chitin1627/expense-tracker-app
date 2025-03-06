package com.example.expensetrackerapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.expensetrackerapp.model.Debt

@Composable
fun DebtScreen(
    debts: List<Debt>,
    addDebt: (Debt) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Your Debts",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (debts.isEmpty()) {
            Text(
                text = "No debts found",
                style = MaterialTheme.typography.bodyLarge
            )
        } else {
            LazyColumn {
                items(debts) { debt ->
                    DebtItem(debt)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                addDebt(
                    Debt(
                        id = null,
                        userId = "USER_ID_HERE", // Replace with actual user ID
                        otherParty = "John Doe",
                        amount = 50.0,
                        isReceivable = false,
                    )
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add Sample Debt", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun DebtItem(debt: Debt) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Other Party: ${debt.otherParty}",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = "Amount: $${debt.amount}",
                style = MaterialTheme.typography.bodyLarge
            )
            Text(
                text = "Date: ${debt.createdDate}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Type: ${if (debt.isReceivable) "Receivable" else "Owed"}",
                color = if (debt.isReceivable) Color.Green else Color.Red,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}
