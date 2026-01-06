package com.example.plana.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.stickyHeader
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.plana.domain.model.Transaction
import com.example.plana.presentation.state.TransactionsUiState
import kotlinx.coroutines.flow.StateFlow
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    uiState: StateFlow<TransactionsUiState>,
    onBack: () -> Unit,
    onEdit: (Long) -> Unit,
    onDelete: (Transaction) -> Unit
) {
    val state by uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Transactions") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        TransactionList(
            transactions = state.transactions,
            onEdit = onEdit,
            onDelete = onDelete,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        )
    }
}

@Composable
private fun TransactionList(
    transactions: List<Transaction>,
    onEdit: (Long) -> Unit,
    onDelete: (Transaction) -> Unit,
    modifier: Modifier = Modifier
) {
    val zoneId = ZoneId.systemDefault()
    val grouped = transactions.groupBy { it.datetime.atZone(zoneId).toLocalDate() }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        grouped.forEach { (date, items) ->
            stickyHeader {
                Text(
                    text = date.toString(),
                    style = MaterialTheme.typography.titleSmall,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                )
            }
            items(items) { item ->
                TransactionRow(
                    transaction = item,
                    onEdit = { onEdit(item.id) },
                    onDelete = { onDelete(item) }
                )
            }
        }
    }
}

@Composable
private fun TransactionRow(
    transaction: Transaction,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(transaction.note ?: "No note", style = MaterialTheme.typography.titleMedium)
            Text("${transaction.type} · ${String.format("%,.2f", transaction.amount)}")
        }
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = onEdit) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit transaction",
                modifier = Modifier.size(20.dp)
            )
        }
        IconButton(onClick = onDelete) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Delete transaction",
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
