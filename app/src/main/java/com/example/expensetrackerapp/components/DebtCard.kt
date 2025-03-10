package com.example.expensetrackerapp.components

import com.example.expensetrackerapp.model.Debt
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun DebtCard(
    debt: Debt,
    isExpanded: Boolean,
    isEditing: Boolean,
    isClickable: Boolean,
    onEditClick: (String) -> Unit,
    onCancelClick: () -> Unit,
    onCardClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
    onSave: suspend (debtId: String, amount: Double, otherParty: String, description: String, completed: Boolean, receivable: Boolean) -> Boolean,
) {
    var showDialog by remember { mutableStateOf(false) }
    var isVisible by remember { mutableStateOf(true) }
    //var isEditing by remember { mutableStateOf(false) }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(animationSpec = tween(300)) + slideInHorizontally(animationSpec = tween(300)),
        exit = fadeOut(animationSpec = tween(300)) + slideOutHorizontally(animationSpec = tween(300))
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            if(debt.receivable)
                                Color(0xFF059212)
                            else
                                Color.Red,
                            MaterialTheme.colorScheme.background
                        ),
                        center = Offset.Infinite,
                        radius = 500f
                    ),
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if(!isExpanded || isEditing) MaterialTheme.colorScheme.background else MaterialTheme.colorScheme.primary
                ),
                border = BorderStroke(2.dp, color = if(debt.receivable) Color(0xFF059212) else Color.Red),
                onClick = { if(!isEditing) onCardClick(debt._id) }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioNoBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                ) {
                    if(isEditing) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            CreateDebtComponent(
                                onSave = onSave,
                                onDismiss = onCancelClick,
                                debtId = debt._id,
                                currentAmount = debt.amount.toString(),
                                currentOtherParty = debt.otherParty,
                                currentDescription = debt.description,
                                currentReceivable = debt.receivable,
                                currentCompleted = debt.completed
                            )
                        }
                    }
                    else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if(!debt.completed) {
                                Text(
                                    text = "${Char(8377)}${debt.amount?:0.0}",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Start
                                )
                            }
                            else {
                                Text(
                                    text = "${Char(8377)}${debt.amount?:0.0}",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Start,
                                    color = if(!isExpanded) Color(0xFF059212) else MaterialTheme.colorScheme.onPrimary
                                )
                            }
                            if(isClickable) {
                                IconButton(
                                    onClick = { showDialog = true},
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Delete,
                                        contentDescription = "Delete Expense",
                                        tint = if(!isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                                IconButton(
                                    onClick = { onEditClick(debt._id) },
                                ) {
                                    Icon(
                                        imageVector = Icons.Outlined.Edit,
                                        contentDescription = "Edit Expense",
                                        tint = if(!isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if(debt.receivable) {
                                Text(
                                    text = "From:",
                                    style = MaterialTheme.typography.titleSmall,
                                    textAlign = TextAlign.Start,
                                    color = if(!isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
                                )
                            }
                            else {
                                Text(
                                    text = "For:",
                                    style = MaterialTheme.typography.titleSmall,
                                    textAlign = TextAlign.Start,
                                    color = if(!isExpanded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onPrimary,
                                )
                            }


                            Text(
                                text = " ${debt.otherParty?:""}",
                                style = MaterialTheme.typography.titleSmall,
                                textAlign = TextAlign.Start
                            )
                        }


                        Spacer(modifier = Modifier.height(4.dp))
                        if(isExpanded) {
                            if(debt.description!="") {
                                Text(
                                    text = debt.description?:"",
                                    style = MaterialTheme.typography.titleSmall,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Start
                                )
                            }
                            else {
                                Text(
                                    text = "No description provided",
                                    style = MaterialTheme.typography.titleSmall,
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Start
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AppAlertDialog(
            title = "Confirm Delete",
            text = "Are you sure you want to delete this debt?",
            onClickConfirm = {
                showDialog = false
                onDeleteClick(debt._id)
                isVisible = false
            },
            onClickDismiss = {
                showDialog = false
            }
        )
    }
}