package com.hassantechno.traceo.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.hassantechno.traceo.ui.theme.TraceoCyan
import com.hassantechno.traceo.ui.theme.TraceoMuted

@Composable
fun TraceoBottomBar(selectedTab: Int, onSelect: (Int) -> Unit) {
    NavigationBar(containerColor = Color(0xFF081321), contentColor = TraceoMuted) {
        NavigationBarItem(
            selected = selectedTab == 0,
            onClick = { onSelect(0) },
            icon = { Icon(Icons.Filled.Home, contentDescription = null) },
            label = { Text("خانه") },
            colors = itemColors(),
        )
        NavigationBarItem(
            selected = selectedTab == 1,
            onClick = { onSelect(1) },
            icon = { Icon(Icons.Filled.List, contentDescription = null) },
            label = { Text("سرویس‌ها") },
            colors = itemColors(),
        )
        NavigationBarItem(
            selected = selectedTab == 2,
            onClick = { onSelect(2) },
            icon = { Icon(Icons.Filled.Info, contentDescription = null) },
            label = { Text("درباره") },
            colors = itemColors(),
        )
    }
}

@Composable
private fun itemColors() = NavigationBarItemDefaults.colors(
    selectedIconColor = TraceoCyan,
    selectedTextColor = TraceoCyan,
    indicatorColor = TraceoCyan.copy(alpha = 0.12f),
    unselectedIconColor = TraceoMuted,
    unselectedTextColor = TraceoMuted,
)
