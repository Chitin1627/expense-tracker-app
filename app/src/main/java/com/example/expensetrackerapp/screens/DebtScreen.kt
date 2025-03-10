package com.example.expensetrackerapp.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensetrackerapp.components.CreateDebtComponent
import com.example.expensetrackerapp.components.DebtCard
import com.example.expensetrackerapp.data.viewmodels.DebtScreenViewModel
import com.example.expensetrackerapp.model.Debt
import com.example.expensetrackerapp.model.DebtRequest

@Composable
fun DebtScreen(
    debts: ArrayList<Debt>,
    onDebtDelete: (String) -> Unit,
) {
    val context = LocalContext.current
    val debtScreenViewModel: DebtScreenViewModel = viewModel()

    val deletedDebt by remember { mutableStateOf(HashMap<String, Boolean>()) }
    var expandedCardId by remember { mutableStateOf<String?>(null) }
    var editingCardId by remember { mutableStateOf<String?>(null) }
    var showCreateDebtDialog by remember { mutableStateOf(false) }
    var currentDebt by remember { mutableStateOf<Debt?>(null) }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .background(MaterialTheme.colorScheme.background),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(Color(0xFF059212), RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Green Border: You will receive money",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(Color.Red, RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Red Border: You have to pay",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(Color(0xFF059212), RoundedCornerShape(4.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Green Amount: Debt is paid",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            if (debts.isNotEmpty()) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(debts) { debt ->
                        println(debt)
                        if (!deletedDebt.containsKey(debt._id)) {
                            DebtCard(
                                debt = debt,
                                isExpanded = expandedCardId == debt._id,
                                isEditing = editingCardId == debt._id,
                                isClickable = editingCardId == null,
                                onEditClick = { debtId ->
                                    currentDebt = debts.find { it._id == debtId }
                                    showCreateDebtDialog = true
                                },
                                onCancelClick = {
                                    editingCardId = null
                                },
                                onCardClick = { debtId ->
                                    println(debtId)
                                    expandedCardId =
                                        if (expandedCardId == debtId) null else debtId
                                },
                                onDeleteClick = { debtId ->
                                    onDebtDelete(debtId)
                                    deletedDebt[debtId] = true
                                },
                                onSave = { debtId, amount, otherParty, description, completed, receivable ->
                                    val editedDebt: Debt = Debt(
                                        _id = debtId,
                                        amount = amount,
                                        otherParty = otherParty,
                                        description = description,
                                        completed = completed,
                                        receivable = receivable
                                    )
                                    println(editedDebt)
                                    debtScreenViewModel.setEditedDebt(editedDebt)
                                    try {
                                        val success = debtScreenViewModel.editDebt(context)
                                        if (success) {
                                            val index = debts.indexOfFirst { it._id == debtId }
                                            if (index != -1) {
                                                debts[index] = debts[index].copy(
                                                    amount = amount,
                                                    otherParty = otherParty,
                                                    description = description,
                                                    completed = completed,
                                                    receivable = receivable
                                                )
                                            }
                                        }
                                        success
                                    } catch (e: Exception) {
                                        false
                                    }
                                }
                            )
                        }
                    }
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Nothing to see here :(",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        Button(
            onClick = {
                currentDebt = null
                showCreateDebtDialog = true
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF059212)
            )
        ) {
            Text(text = "+")
        }
    }

    if (showCreateDebtDialog) {
        println("Current Debt: $currentDebt") // Debugging
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties()
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.background,
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
            ) {
                CreateDebtComponent(
                    onSave = { debtId, amount, otherParty, description, completed, receivable ->
                        val success = if (currentDebt == null) {
                            val debt = DebtRequest(
                                amount = amount,
                                otherParty = otherParty,
                                description = description,
                                completed = completed,
                                receivable = receivable
                            )
                            println("ADD DEBT " + debt)
                            val newDebt = debtScreenViewModel.addDebt(debt, context)
                            if (newDebt._id != "") {
                                debts.add(newDebt)
                                true
                            } else false
                        } else {
                            val debt = Debt(
                                _id = debtId,
                                amount = amount,
                                otherParty = otherParty,
                                description = description,
                                completed = completed,
                                receivable = receivable
                            )
                            debtScreenViewModel.setEditedDebt(debt)
                            val suc = debtScreenViewModel.editDebt(context)
                            if (suc) {
                                val index = debts.indexOfFirst { it._id == debtId }
                                if (index != -1) {
                                    debts[index] = debts[index].copy(
                                        amount = amount,
                                        otherParty = otherParty,
                                        description = description,
                                        completed = completed,
                                        receivable = receivable
                                    )
                                }
                                currentDebt = null // Reset after editing
                            }
                            suc
                        }
                        if (success) {
                            showCreateDebtDialog = false
                        }
                        success
                    },
                    onDismiss = {
                        showCreateDebtDialog = false
                        currentDebt = null
                    },
                    debtId = currentDebt?._id ?: "",
                    currentAmount = currentDebt?.amount?.toString() ?: "",
                    currentOtherParty = currentDebt?.otherParty ?: "",
                    currentDescription = currentDebt?.description ?: "",
                    currentReceivable = currentDebt?.receivable ?: false,
                    currentCompleted = currentDebt?.completed ?: false
                )
            }
        }
    }
}