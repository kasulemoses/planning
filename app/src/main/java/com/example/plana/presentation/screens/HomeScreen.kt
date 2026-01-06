package com.example.plana.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.plana.domain.model.DailySummary
import com.example.plana.presentation.state.HomeUiState
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: StateFlow<HomeUiState>,
    onAddTransaction: () -> Unit,
    onViewTransactions: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val state by uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Daily Overview") },
                actions = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddTransaction) {
                Icon(Icons.Default.Add, contentDescription = "Quick add transaction")
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                OverviewHeader(
                    today = state.dailySummary?.todayTotal,
                    yesterday = state.dailySummary?.yesterdayTotal,
                    currency = state.currency,
                    hideAmounts = state.hideAmounts
                )
            }
            item {
                TrendsCard(
                    last7Days = state.dailySummary?.last7Days.orEmpty(),
                    currency = state.currency,
                    hideAmounts = state.hideAmounts
                )
            }
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AssistChip(
                        onClick = onViewTransactions,
                        label = { Text("Transactions") },
                        leadingIcon = {
                            Icon(Icons.Default.CalendarMonth, contentDescription = null)
                        },
                        colors = AssistChipDefaults.assistChipColors()
                    )
                    AssistChip(
                        onClick = onViewTransactions,
                        label = { Text("Analytics") },
                        leadingIcon = {
                            Icon(Icons.Default.Analytics, contentDescription = null)
                        }
                    )
                }
            }
            item {
                Text("Top categories today", style = MaterialTheme.typography.titleMedium)
            }
            items(state.topCategories) { category ->
                CategoryCard(
                    name = category.id?.toString() ?: "Uncategorized",
                    total = category.total,
                    currency = state.currency,
                    hideAmounts = state.hideAmounts
                )
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
private fun OverviewHeader(
    today: Double?,
    yesterday: Double?,
    currency: String,
    hideAmounts: Boolean
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("Daily Overview", style = MaterialTheme.typography.titleLarge)
                    Text(
                        text = "Track spending at a glance",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Surface(
                    modifier = Modifier.clip(MaterialTheme.shapes.large),
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Icon(
                        modifier = Modifier.padding(10.dp),
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = null
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SummaryCard(
                    title = "Today",
                    value = formatAmount(today ?: 0.0, currency, hideAmounts)
                )
                SummaryCard(
                    title = "Yesterday",
                    value = formatAmount(yesterday ?: 0.0, currency, hideAmounts)
                )
            }
        }
    }
}

@Composable
private fun SummaryCard(title: String, value: String) {
    Card(
        modifier = Modifier.weight(1f),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.labelLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, style = MaterialTheme.typography.headlineSmall)
        }
    }
}

@Composable
private fun TrendsCard(
    last7Days: List<DailySummary>,
    currency: String,
    hideAmounts: Boolean
) {
    val maxTotal = last7Days.maxOfOrNull { it.total.absoluteValue }?.coerceAtLeast(1.0) ?: 1.0
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Last 7 days trend", style = MaterialTheme.typography.titleMedium)
            if (last7Days.isEmpty()) {
                Text(
                    text = "No recent activity yet.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                last7Days.forEach { day ->
                    TrendRow(
                        label = day.day.toString(),
                        amount = day.total,
                        progress = (day.total.absoluteValue / maxTotal)
                            .toFloat()
                            .coerceIn(0f, 1f),
                        currency = currency,
                        hideAmounts = hideAmounts
                    )
                }
            }
        }
    }
}

@Composable
private fun TrendRow(
    label: String,
    amount: Double,
    progress: Float,
    currency: String,
    hideAmounts: Boolean
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, style = MaterialTheme.typography.labelLarge)
            Text(
                formatAmount(amount, currency, hideAmounts),
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold
            )
        }
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(MaterialTheme.shapes.small),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun CategoryCard(
    name: String,
    total: Double,
    currency: String,
    hideAmounts: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(36.dp),
                    shape = MaterialTheme.shapes.medium,
                    color = MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = name.firstOrNull()?.uppercaseChar()?.toString() ?: "?",
                        modifier = Modifier.padding(8.dp),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = name, style = MaterialTheme.typography.titleSmall)
            }
            Text(
                text = formatAmount(total, currency, hideAmounts),
                fontWeight = FontWeight.Bold
            )
        }
    }
}

private fun formatAmount(amount: Double, currency: String, hideAmounts: Boolean): String {
    if (hideAmounts) return "••••"
    return "$currency ${String.format("%,.2f", amount)}"
}
