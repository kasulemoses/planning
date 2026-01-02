package com.example.plana.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.plana.domain.model.TransactionType
import com.example.plana.presentation.AddEditTransactionUiState
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditTransactionScreen(
    uiState: StateFlow<AddEditTransactionUiState>,
    onSave: () -> Unit,
    onBack: () -> Unit,
    onAmountChange: (String) -> Unit,
    onNoteChange: (String) -> Unit,
    onCategoryChange: (Long?) -> Unit,
    onTypeChange: (TransactionType) -> Unit
) {
    val state by uiState.collectAsState()
    var categoriesExpanded by remember { mutableStateOf(false) }
    var typeExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Transaction") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = onSave) {
                        Icon(Icons.Default.Save, contentDescription = "Save")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (state.formState.errorMessage != null) {
                Text(state.formState.errorMessage ?: "", color = MaterialTheme.colorScheme.error)
            }
            OutlinedTextField(
                value = state.formState.amount,
                onValueChange = onAmountChange,
                label = { Text("Amount (${state.currency})") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = state.formState.note,
                onValueChange = onNoteChange,
                label = { Text("Note") },
                modifier = Modifier.fillMaxWidth()
            )
            TextButton(onClick = { typeExpanded = true }) {
                Text("Type: ${state.formState.type}")
            }
            DropdownMenu(expanded = typeExpanded, onDismissRequest = { typeExpanded = false }) {
                TransactionType.entries.forEach { type ->
                    DropdownMenuItem(
                        text = { Text(type.name) },
                        onClick = {
                            onTypeChange(type)
                            typeExpanded = false
                        }
                    )
                }
            }
            TextButton(onClick = { categoriesExpanded = true }) {
                Text(
                    text = state.categories
                        .firstOrNull { it.id == state.formState.categoryId }
                        ?.name ?: "Category"
                )
            }
            DropdownMenu(expanded = categoriesExpanded, onDismissRequest = { categoriesExpanded = false }) {
                DropdownMenuItem(
                    text = { Text("Uncategorized") },
                    onClick = {
                        onCategoryChange(null)
                        categoriesExpanded = false
                    }
                )
                state.categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category.name) },
                        onClick = {
                            onCategoryChange(category.id)
                            categoriesExpanded = false
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Quick add keeps defaults so you can log in 2–3 taps.",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
