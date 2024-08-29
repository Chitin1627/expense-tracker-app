package com.example.expensetrackerapp.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import com.example.expensetrackerapp.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateSelectorButton(
    onDateSelected: (String) -> Unit,
    text: String,
    modifier: Modifier = Modifier
) {
    val datePickerState = rememberDatePickerState(selectableDates = object : SelectableDates {
        override fun isSelectableDate(utcTimeMillis: Long) : Boolean {
            return utcTimeMillis-19800000 <= System.currentTimeMillis()
        }
    })
    val selectedDate = datePickerState.selectedDateMillis?.let {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.format(it)
        date
    } ?: ""

    var okButtonEnabled by remember { mutableStateOf(false) }
    var showDatePickerDialog by remember { mutableStateOf(false) }
    if(selectedDate!="") {
        okButtonEnabled = true
    }

    Button(
        onClick = { showDatePickerDialog = true },
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
        //border = BorderStroke(2.dp, MaterialTheme.colorScheme.onBackground)
    ) {
        Text(text = text)
    }

    if(showDatePickerDialog) {
        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                Button(
                    onClick = {
                        onDateSelected(selectedDate)
                        showDatePickerDialog = false
                    },
                    enabled = okButtonEnabled,
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    Text(text = stringResource(R.string.ok))
                }
            },
            dismissButton = {
                Button(onClick = {
                    showDatePickerDialog = false
                }) {
                    Text(text = stringResource(R.string.cancel))
                }
            },
            //colors = DatePickerColors()
//            colors = DatePickerDefaults.colors(
//                MaterialTheme.colorScheme.secondaryContainer
//            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
