package com.example.plana.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.SouthWest
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.plana.presentation.state.CalendarDay
import com.example.plana.presentation.state.CalendarHighlight
import com.example.plana.presentation.state.CalendarUiState
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.WeekFields
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    uiState: StateFlow<CalendarUiState>,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    val state by uiState.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Calendar") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            MonthSwitcher(
                month = state.month,
                onPreviousMonth = onPreviousMonth,
                onNextMonth = onNextMonth
            )
            MonthlySummary(
                monthIncomeTotal = state.monthIncomeTotal,
                monthExpenseTotal = state.monthExpenseTotal,
                currency = state.currency,
                hideAmounts = state.hideAmounts
            )
            CalendarGrid(
                days = state.days,
                currency = state.currency,
                hideAmounts = state.hideAmounts
            )
            HighlightsSection(
                highlights = state.highlights,
                currency = state.currency,
                hideAmounts = state.hideAmounts
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun MonthSwitcher(
    month: YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(Icons.Default.ChevronLeft, contentDescription = "Previous month")
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = month.format(DateTimeFormatter.ofPattern("MMMM yyyy")),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Monthly overview",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(onClick = onNextMonth) {
            Icon(Icons.Default.ChevronRight, contentDescription = "Next month")
        }
    }
}

@Composable
private fun MonthlySummary(
    monthIncomeTotal: Double,
    monthExpenseTotal: Double,
    currency: String,
    hideAmounts: Boolean
) {
    val netTotal = monthIncomeTotal - monthExpenseTotal
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SummaryCard(
            title = "Income",
            amount = monthIncomeTotal,
            currency = currency,
            hideAmounts = hideAmounts,
            icon = Icons.Default.TrendingUp,
            accent = MaterialTheme.colorScheme.tertiary
        )
        SummaryCard(
            title = "Expenses",
            amount = monthExpenseTotal,
            currency = currency,
            hideAmounts = hideAmounts,
            icon = Icons.Default.SouthWest,
            accent = MaterialTheme.colorScheme.error
        )
        SummaryCard(
            title = "Net",
            amount = netTotal,
            currency = currency,
            hideAmounts = hideAmounts,
            icon = Icons.Default.Savings,
            accent = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun SummaryCard(
    title: String,
    amount: Double,
    currency: String,
    hideAmounts: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    accent: Color
) {
    Card(
        modifier = Modifier.weight(1f),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BoxedIcon(icon = icon, tint = accent)
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = title, style = MaterialTheme.typography.labelLarge)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = formatAmount(amount, currency, hideAmounts),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun BoxedIcon(icon: androidx.compose.ui.graphics.vector.ImageVector, tint: Color) {
    BoxedSurface {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = tint,
            modifier = Modifier.size(18.dp)
        )
    }
}

@Composable
private fun BoxedSurface(content: @Composable () -> Unit) {
    Surface(
        modifier = Modifier
            .size(28.dp)
            .clip(CircleShape),
        color = MaterialTheme.colorScheme.background,
        tonalElevation = 2.dp
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            content()
        }
    }
}

@Composable
private fun CalendarGrid(
    days: List<CalendarDay>,
    currency: String,
    hideAmounts: Boolean
) {
    val weekFields = WeekFields.of(Locale.getDefault())
    val daysOfWeek = (0 until 7).map { offset ->
        weekFields.firstDayOfWeek.plus(offset.toLong())
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(modifier = Modifier.fillMaxWidth()) {
                daysOfWeek.forEach { dayOfWeek ->
                    Text(
                        text = dayOfWeek.getDisplayName(java.time.format.TextStyle.SHORT, Locale.getDefault()),
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            days.chunked(7).forEach { week ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    week.forEach { day ->
                        DayCell(
                            day = day,
                            currency = currency,
                            hideAmounts = hideAmounts
                        )
                    }
                }
                Spacer(modifier = Modifier.height(6.dp))
            }
        }
    }
}

@Composable
private fun DayCell(
    day: CalendarDay,
    currency: String,
    hideAmounts: Boolean
) {
    val isToday = day.date == LocalDate.now()
    val backgroundColor = if (isToday) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.background
    }
    val textColor = if (day.isInCurrentMonth) {
        MaterialTheme.colorScheme.onBackground
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        modifier = Modifier
            .weight(1f)
            .height(72.dp),
        color = backgroundColor,
        shape = RoundedCornerShape(12.dp),
        tonalElevation = if (isToday) 3.dp else 0.dp
    ) {
        Column(
            modifier = Modifier.padding(6.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = day.date.dayOfMonth.toString(),
                style = MaterialTheme.typography.labelMedium,
                color = textColor
            )
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                if (day.expenseTotal > 0) {
                    Text(
                        text = "-${formatCompactAmount(day.expenseTotal, currency, hideAmounts)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                if (day.incomeTotal > 0) {
                    Text(
                        text = "+${formatCompactAmount(day.incomeTotal, currency, hideAmounts)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
                if (day.expenseTotal == 0.0 && day.incomeTotal == 0.0) {
                    Text(
                        text = "—",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun HighlightsSection(
    highlights: List<CalendarHighlight>,
    currency: String,
    hideAmounts: Boolean
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Highlights",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (highlights.isEmpty()) {
                Text(
                    text = "No transactions yet for this month.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                highlights.forEach { highlight ->
                    HighlightRow(
                        highlight = highlight,
                        currency = currency,
                        hideAmounts = hideAmounts
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun HighlightRow(
    highlight: CalendarHighlight,
    currency: String,
    hideAmounts: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = highlight.date.format(DateTimeFormatter.ofPattern("EEE, MMM d")),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (highlight.expenseTotal > 0) {
                    Text(
                        text = "Spent ${formatCompactAmount(highlight.expenseTotal, currency, hideAmounts)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                if (highlight.incomeTotal > 0) {
                    Text(
                        text = "Earned ${formatCompactAmount(highlight.incomeTotal, currency, hideAmounts)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                }
            }
        }
        Surface(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary),
            color = MaterialTheme.colorScheme.primary
        ) {}
    }
}

private fun formatAmount(amount: Double, currency: String, hideAmounts: Boolean): String {
    if (hideAmounts) return "••••"
    return "$currency ${String.format("%,.2f", amount)}"
}

private fun formatCompactAmount(amount: Double, currency: String, hideAmounts: Boolean): String {
    if (hideAmounts) return "••••"
    return "$currency ${String.format("%,.0f", amount)}"
}
