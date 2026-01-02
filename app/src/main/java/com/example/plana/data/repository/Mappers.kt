package com.example.plana.data.repository

import com.example.plana.data.local.dao.AccountTotal
import com.example.plana.data.local.dao.CategoryTotal
import com.example.plana.data.local.entities.AccountEntity
import com.example.plana.data.local.entities.BudgetEntity
import com.example.plana.data.local.entities.CategoryEntity
import com.example.plana.data.local.entities.RecurringRuleEntity
import com.example.plana.data.local.entities.TagEntity
import com.example.plana.data.local.entities.TransactionEntity
import com.example.plana.domain.model.Account
import com.example.plana.domain.model.AggregateTotal
import com.example.plana.domain.model.Budget
import com.example.plana.domain.model.Category
import com.example.plana.domain.model.RecurringRule
import com.example.plana.domain.model.Tag
import com.example.plana.domain.model.Transaction
import com.example.plana.domain.model.TransactionType

fun TransactionEntity.toDomain(): Transaction = Transaction(
    id = id,
    type = TransactionType.valueOf(type.name),
    amount = amount,
    datetime = datetime,
    categoryId = categoryId,
    accountId = accountId,
    note = note,
    paymentMethod = paymentMethod,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun Transaction.toEntity(): TransactionEntity = TransactionEntity(
    id = id,
    type = com.example.plana.data.local.entities.TransactionType.valueOf(type.name),
    amount = amount,
    datetime = datetime,
    categoryId = categoryId,
    accountId = accountId,
    note = note,
    paymentMethod = paymentMethod,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun CategoryEntity.toDomain(): Category = Category(
    id = id,
    name = name,
    iconKey = iconKey,
    color = color,
    type = com.example.plana.domain.model.CategoryType.valueOf(type.name),
    isArchived = isArchived,
    spendingLimit = spendingLimit
)

fun Category.toEntity(): CategoryEntity = CategoryEntity(
    id = id,
    name = name,
    iconKey = iconKey,
    color = color,
    type = com.example.plana.data.local.entities.CategoryType.valueOf(type.name),
    isArchived = isArchived,
    spendingLimit = spendingLimit
)

fun AccountEntity.toDomain(): Account = Account(
    id = id,
    name = name,
    type = type,
    color = color,
    isArchived = isArchived,
    openingBalance = openingBalance
)

fun Account.toEntity(): AccountEntity = AccountEntity(
    id = id,
    name = name,
    type = type,
    color = color,
    isArchived = isArchived,
    openingBalance = openingBalance
)

fun BudgetEntity.toDomain(): Budget = Budget(
    id = id,
    categoryId = categoryId,
    limitAmount = limitAmount,
    period = com.example.plana.domain.model.BudgetPeriod.valueOf(period.name),
    startDate = startDate
)

fun Budget.toEntity(): BudgetEntity = BudgetEntity(
    id = id,
    categoryId = categoryId,
    limitAmount = limitAmount,
    period = com.example.plana.data.local.entities.BudgetPeriod.valueOf(period.name),
    startDate = startDate
)

fun TagEntity.toDomain(): Tag = Tag(id = id, name = name)

fun Tag.toEntity(): TagEntity = TagEntity(id = id, name = name)

fun RecurringRuleEntity.toDomain(): RecurringRule = RecurringRule(
    id = id,
    templateAmount = templateAmount,
    templateNote = templateNote,
    templateCategoryId = templateCategoryId,
    templateAccountId = templateAccountId,
    templatePaymentMethod = templatePaymentMethod,
    templateType = TransactionType.valueOf(templateType.name),
    frequency = com.example.plana.domain.model.RecurringFrequency.valueOf(frequency.name),
    interval = interval,
    nextRunAt = nextRunAt,
    endAt = endAt,
    isActive = isActive
)

fun CategoryTotal.toDomain(): AggregateTotal = AggregateTotal(id = id, total = total)

fun AccountTotal.toDomain(): AggregateTotal = AggregateTotal(id = id, total = total)
