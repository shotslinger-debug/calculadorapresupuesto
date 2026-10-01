package com.upchiapas.presupuesto.budget.view.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.upchiapas.presupuesto.budget.model.BudgetCategory
import java.text.NumberFormat

@Composable
fun CategorySliderItem(
    category: BudgetCategory,
    onPercentageChange: (Float) -> Unit
) {
    val currencyFormat = NumberFormat.getCurrencyInstance()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Icono, Nombre y Monto calculado
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = category.name,
                    tint = category.color,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = currencyFormat.format(category.amount),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = category.color
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Texto del % y Slider interactivo
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${category.percentage.toInt()}%",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.width(40.dp)
                )
                Slider(
                    value = category.percentage,
                    onValueChange = onPercentageChange,
                    valueRange = 0f..100f,
                    modifier = Modifier.weight(1f),
                    colors = SliderDefaults.colors(
                        thumbColor = category.color,
                        activeTrackColor = category.color
                    )
                )
            }
            
            // Barra de progreso visual
            LinearProgressIndicator(
                progress = { category.percentage / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp),
                color = category.color,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}
