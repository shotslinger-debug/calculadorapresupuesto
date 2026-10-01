package com.upchiapas.presupuesto.budget.model

data class BudgetUiState(
    val incomeInput: String = "",
    val incomeAmount: Double = 0.0,
    val categories: List<BudgetCategory> = emptyList(),
    val totalPercentageAssigned: Float = 0f,
    val totalAmountAssigned: Double = 0.0,
    val remainingAmount: Double = 0.0,
    val isRoundingEnabled: Boolean = false,
    val presetSelected: String = "Personalizado"
)
