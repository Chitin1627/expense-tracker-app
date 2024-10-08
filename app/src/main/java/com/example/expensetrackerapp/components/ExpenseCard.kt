package com.example.expensetrackerapp.components

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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.expensetrackerapp.model.Expense
import com.example.expensetrackerapp.ui.theme.Teal

@Composable
fun ExpenseCard(
    expense: Expense,
    category: String,
    isExpanded: Boolean,
    isEditing: Boolean,
    isClickable: Boolean,
    onEditClick: (String) -> Unit,
    onCancelClick: () -> Unit,
    onCardClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
    onSave: suspend (expenseId: String, amount: Double, category: String, type: String, description: String, date: String, created_at: String) -> Boolean,
    categoryNameMap: HashMap<String, String>
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
                        colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.background),
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
                border = BorderStroke(2.dp, color = MaterialTheme.colorScheme.primary),
                onClick = { if(!isEditing) onCardClick(expense._id) }
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
                            CreateExpenseComponent(
                                onSave = onSave,
                                onDismiss = onCancelClick,
                                categoryNameMap = categoryNameMap,
                                expenseId = expense._id,
                                currentAmount = expense.amount.toString(),
                                currentDescription = expense.description,
                                currentCategory = category,
                                currentDate = expense.date,
                                currentType = expense.type
                            )
                        }
                    }
                    else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if(expense.type=="DEBIT") {
                                Text(
                                    text = "${Char(8377)}${expense.amount}",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.weight(1f),
                                    textAlign = TextAlign.Start
                                )
                            }
                            else {
                                Text(
                                    text = "+ ${Char(8377)}${expense.amount}",
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
                                    onClick = { onEditClick(expense._id) },
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

                        Text(
                            text = "Category: $category",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        if(isExpanded) {
                            if(expense.description!="") {
                                Text(
                                    text = expense.description,
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
            text = "Are you sure you want to delete this expense?",
            onClickConfirm = {
                showDialog = false
                onDeleteClick(expense._id)
                isVisible = false
            },
            onClickDismiss = {
                showDialog = false
            }
        )
    }
}