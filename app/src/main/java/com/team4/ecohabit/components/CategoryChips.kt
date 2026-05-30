package com.team4.ecohabit.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.team4.ecohabit.ui.theme.activeIcon
import com.team4.ecohabit.ui.theme.activeMenu
import com.team4.ecohabit.ui.theme.grayGreen

@Composable
fun CategorySection(
    categories: List<String>,
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {

    Column {

        Text("Category")

        Spacer(Modifier.height(12.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            categories.forEach { category ->

                FilterChip(
                    selected = category == selectedCategory,
                    onClick = {
                        onCategorySelected(category)
                    },
                    label = {
                        Text(category)
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = activeMenu,
                        selectedLabelColor = activeIcon,
                        containerColor = grayGreen
                    )
                )
            }
        }
    }
}