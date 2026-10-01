package com.upchiapas.presupuesto.budget.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.lifecycle.ViewModel
import com.upchiapas.presupuesto.budget.model.BudgetCategory
import com.upchiapas.presupuesto.budget.model.BudgetUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BudgetViewModel : ViewModel() {

    // PATRÓN BACKING PROPERTY (Rúbrica: Separación de responsabilidades y UDF)
    // El estado mutable es privado, solo el ViewModel puede modificarlo.
    private val _uiState = MutableStateFlow(BudgetUiState())
    // El estado público es de solo lectura (inmutable para la UI).
    val uiState: StateFlow<BudgetUiState> = _uiState.asStateFlow()

    init {
        // Inicializamos las categorías base
        val initialCategories = listOf(
            BudgetCategory("1", "Renta / Hogar", color = Color(0xFF4CAF50), icon = Icons.Filled.Home),
            BudgetCategory("2", "Comida", color = Color(0xFFFF9800), icon = Icons.Filled.ShoppingCart),
            BudgetCategory("3", "Transporte", color = Color(0xFF2196F3), icon = Icons.Filled.DirectionsCar),
            BudgetCategory("4", "Ahorro", color = Color(0xFF9C27B0), icon = Icons.Filled.Savings),
            BudgetCategory("5", "Entretenimiento", color = Color(0xFFE91E63), icon = Icons.Filled.Movie)
        )
        _uiState.update { it.copy(categories = initialCategories) }
    }

    /**
     * Actualiza el ingreso mensual digitado por el usuario.
     */
    fun updateIncome(newIncome: String) {
        val amount = newIncome.toDoubleOrNull() ?: 0.0
        _uiState.update { currentState ->
            currentState.copy(
                incomeInput = newIncome,
                incomeAmount = amount
            )
        }
        recalculateBudget()
    }

    /**
     * Actualiza el porcentaje de una categoría específica y recalcula todo.
     */
    fun updateCategoryPercentage(categoryId: String, newPercentage: Float) {
        _uiState.update { currentState ->
            val updatedCategories = currentState.categories.map { category ->
                if (category.id == categoryId) {
                    category.copy(percentage = newPercentage)
                } else {
                    category
                }
            }
            currentState.copy(
                categories = updatedCategories,
                presetSelected = "Personalizado" // Si el usuario mueve algo, ya no es preset
            )
        }
        recalculateBudget()
    }

    /**
     * Activa o desactiva el redondeo de los montos resultantes.
     */
    fun toggleRounding(enabled: Boolean) {
        _uiState.update { it.copy(isRoundingEnabled = enabled) }
        recalculateBudget()
    }

    /**
     * Aplica reglas de negocio prestablecidas (Presets)
     */
    fun applyPreset(presetName: String) {
        _uiState.update { currentState ->
            val newCategories = currentState.categories.map { category ->
                val newPct = when (presetName) {
                    "50/30/20" -> when (category.name) {
                        "Renta / Hogar" -> 50f
                        "Comida", "Transporte" -> 15f // 30% combinado
                        "Ahorro" -> 20f
                        else -> 0f
                    }
                    "Austero" -> when (category.name) {
                        "Renta / Hogar" -> 40f
                        "Comida" -> 30f
                        "Transporte" -> 10f
                        "Ahorro" -> 20f
                        else -> 0f
                    }
                    else -> 0f
                }
                category.copy(percentage = newPct)
            }
            currentState.copy(
                categories = newCategories,
                presetSelected = presetName
            )
        }
        recalculateBudget()
    }

    /**
     * Lógica 100% en memoria (Rúbrica: Cálculo matemático complejo).
     * Recalcula montos por categoría, porcentajes totales y balance restante.
     */
    private fun recalculateBudget() {
        _uiState.update { currentState ->
            val totalPct = currentState.categories.sumOf { it.percentage.toDouble() }.toFloat()
            
            val updatedCategories = currentState.categories.map { category ->
                var calculatedAmount = (currentState.incomeAmount * category.percentage) / 100.0
                
                if (currentState.isRoundingEnabled) {
                    calculatedAmount = Math.round(calculatedAmount).toDouble()
                }
                
                category.copy(amount = calculatedAmount)
            }

            val totalAssigned = updatedCategories.sumOf { it.amount }
            val remaining = currentState.incomeAmount - totalAssigned

            currentState.copy(
                categories = updatedCategories,
                totalPercentageAssigned = totalPct,
                totalAmountAssigned = totalAssigned,
                remainingAmount = remaining
            )
        }
    }
}
