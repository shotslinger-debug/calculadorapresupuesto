package com.upchiapas.presupuesto.budget.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class BudgetCategory(
    val id: String,
    val name: String,
    val percentage: Float = 0f, // 0.0 a 100.0
    val amount: Double = 0.0,
    val color: Color,
    val icon: ImageVector
)
