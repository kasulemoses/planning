@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package com.example.plana.presentation.screens

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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

@Composable
fun TransactionsScreen(
    uiState: StateFlow<TransactionsUiState>,
    onBack: () -> Unit
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
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        )
    }
}

@Composable
private fun TransactionList(transactions: List<Transaction>, modifier: Modifier = Modifier) {
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
                TransactionRow(transaction = item)
            }
        }
    }
}

@Composable
private fun TransactionRow(transaction: Transaction) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(transaction.note ?: "No note", style = MaterialTheme.typography.titleMedium)
        Text("${transaction.type} · ${String.format("%,.2f", transaction.amount)}")
    }
}
