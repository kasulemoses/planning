package com.example.plana.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.plana.domain.model.Transaction
import com.example.plana.domain.model.TransactionType
import com.example.plana.domain.repository.CategoryRepository
import com.example.plana.domain.repository.SettingsRepository
import com.example.plana.domain.usecase.AddTransactionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
import javax.inject.Inject

@HiltViewModel
class AddEditTransactionViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase,
    categoryRepository: CategoryRepository,
    settingsRepository: SettingsRepository
) : ViewModel() {
    private val formState = MutableStateFlow(TransactionFormState())

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

    fun save(onSaved: () -> Unit) {
        val form = formState.value
        val amount = form.amount.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            formState.value = form.copy(errorMessage = "Enter a valid amount")
            return
        }

        viewModelScope.launch {
            addTransactionUseCase(
                Transaction(
                    id = 0,
                    type = form.type,
                    amount = amount,
                    datetime = Instant.now(),
                    categoryId = form.categoryId,
                    accountId = null,
                    note = form.note.ifBlank { null },
                    paymentMethod = form.paymentMethod.ifBlank { null },
                    createdAt = Instant.now(),
                    updatedAt = Instant.now()
                )
            )
            formState.value = TransactionFormState()
            onSaved()
        }
    }
}

data class AddEditTransactionUiState(
    val formState: TransactionFormState = TransactionFormState(),
    val categories: List<com.example.plana.domain.model.Category> = emptyList(),
    val currency: String = "USD"
)

data class TransactionFormState(
    val amount: String = "",
    val note: String = "",
    val paymentMethod: String = "",
    val categoryId: Long? = null,
    val type: TransactionType = TransactionType.EXPENSE,
    val errorMessage: String? = null
)
