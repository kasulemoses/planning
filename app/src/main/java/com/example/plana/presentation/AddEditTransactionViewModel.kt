package com.example.plana.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plana.domain.model.Transaction
import com.example.plana.domain.model.TransactionType
import com.example.plana.domain.repository.CategoryRepository
import com.example.plana.domain.repository.TransactionRepository
import com.example.plana.domain.repository.SettingsRepository
import com.example.plana.domain.usecase.AddTransactionUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant

class AddEditTransactionViewModel(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val transactionRepository: TransactionRepository,
    categoryRepository: CategoryRepository,
    settingsRepository: SettingsRepository
) : ViewModel() {
    private val formState = MutableStateFlow(TransactionFormState())
    private var editingTransaction: Transaction? = null

    val uiState: StateFlow<AddEditTransactionUiState> = combine(
        formState,
        categoryRepository.observeActive(),
        settingsRepository.currency
    ) { form, categories, currency ->
        AddEditTransactionUiState(
            formState = form,
            categories = categories,
            currency = currency
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AddEditTransactionUiState())

    fun updateAmount(value: String) {
        formState.value = formState.value.copy(amount = value)
    }

    fun updateNote(value: String) {
        formState.value = formState.value.copy(note = value)
    }

    fun updateCategory(categoryId: Long?) {
        formState.value = formState.value.copy(categoryId = categoryId)
    }

    fun updateType(type: TransactionType) {
        formState.value = formState.value.copy(type = type)
    }

    fun loadTransaction(transactionId: Long?) {
        if (transactionId == null || transactionId == formState.value.transactionId) return
        viewModelScope.launch {
            val transaction = transactionRepository.getById(transactionId) ?: return@launch
            editingTransaction = transaction
            formState.value = formState.value.copy(
                transactionId = transaction.id,
                amount = transaction.amount.toString(),
                note = transaction.note.orEmpty(),
                paymentMethod = transaction.paymentMethod.orEmpty(),
                categoryId = transaction.categoryId,
                type = transaction.type,
                errorMessage = null
            )
        }
    }

    fun save(onSaved: () -> Unit) {
        val form = formState.value
        val amount = form.amount.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            formState.value = form.copy(errorMessage = "Enter a valid amount")
            return
        }

        viewModelScope.launch {
            val now = Instant.now()
            val baseTransaction = editingTransaction
            addTransactionUseCase(
                Transaction(
                    id = form.transactionId ?: 0,
                    type = form.type,
                    amount = amount,
                    datetime = baseTransaction?.datetime ?: now,
                    categoryId = form.categoryId,
                    accountId = null,
                    note = form.note.ifBlank { null },
                    paymentMethod = form.paymentMethod.ifBlank { null },
                    createdAt = baseTransaction?.createdAt ?: now,
                    updatedAt = now
                )
            )
            formState.value = TransactionFormState()
            editingTransaction = null
            onSaved()
        }
    }
}

data class AddEditTransactionUiState(
    val formState: TransactionFormState = TransactionFormState(),
    val categories: List<com.example.plana.domain.model.Category> = emptyList(),
    val currency: String = "USD"
) {
    val isEditing: Boolean = formState.transactionId != null
}

data class TransactionFormState(
    val transactionId: Long? = null,
    val amount: String = "",
    val note: String = "",
    val paymentMethod: String = "",
    val categoryId: Long? = null,
    val type: TransactionType = TransactionType.EXPENSE,
    val errorMessage: String? = null
)
