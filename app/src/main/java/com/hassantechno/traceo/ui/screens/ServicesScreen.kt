package com.hassantechno.traceo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hassantechno.traceo.data.Service
import com.hassantechno.traceo.data.ServiceCategory
import com.hassantechno.traceo.phone.PhoneNumber
import com.hassantechno.traceo.ui.components.ServiceTile
import com.hassantechno.traceo.ui.theme.TraceoAccent
import com.hassantechno.traceo.ui.theme.TraceoCyan
import com.hassantechno.traceo.ui.theme.TraceoMuted
import com.hassantechno.traceo.ui.theme.TraceoOutline
import com.hassantechno.traceo.ui.theme.TraceoText
import com.hassantechno.traceo.ui.theme.TraceoWarning

enum class ServiceFilter(val label: String) {
    ALL("همه"),
    IRANIAN("ایرانی"),
    INTERNATIONAL("بین‌المللی"),
}

@Composable
fun ServicesScreen(
    phone: PhoneNumber?,
    services: List<Service>,
    installedIds: Set<String>,
    query: String,
    onQueryChange: (String) -> Unit,
    filterIndex: Int,
    onFilterChange: (Int) -> Unit,
    onServiceClick: (Service) -> Unit,
    onEditPhone: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val filter = ServiceFilter.values().getOrElse(filterIndex) { ServiceFilter.ALL }
    val visibleServices = remember(services, query, filter) {
        val text = query.trim()
        services.filter { service ->
            val categoryMatches = when (filter) {
                ServiceFilter.ALL -> true
                ServiceFilter.IRANIAN -> service.category == ServiceCategory.IRANIAN
                ServiceFilter.INTERNATIONAL -> service.category == ServiceCategory.INTERNATIONAL
            }
            val queryMatches = text.isBlank() ||
                service.name.contains(text, ignoreCase = true) ||
                service.description.contains(text, ignoreCase = true)
            categoryMatches && queryMatches
        }
    }
    val keyboard = LocalSoftwareKeyboardController.current

    LazyVerticalGrid(
        columns = GridCells.Adaptive(104.dp),
        modifier = modifier.imePadding(),
        contentPadding = PaddingValues(start = 18.dp, end = 18.dp, top = 18.dp, bottom = 26.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    "انتخاب پیام‌رسان",
                    color = TraceoText,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    if (phone == null) {
                        "شماره را وارد کن تا لینک مستقیم فعال شود."
                    } else {
                        "برای شروع گفتگو، یک سرویس انتخاب کن."
                    },
                    color = TraceoMuted,
                    fontSize = 12.sp,
                )
            }
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            PhoneSummary(phone = phone, onEditPhone = onEditPhone)
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                placeholder = { Text("جست‌وجوی پیام‌رسان...", color = TraceoMuted, fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Filled.Search, null, tint = TraceoMuted) },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { onQueryChange("") }) {
                            Icon(Icons.Filled.Clear, contentDescription = "پاک کردن", tint = TraceoMuted)
                        }
                    }
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { keyboard?.hide() }),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = TraceoCyan,
                    unfocusedBorderColor = TraceoOutline,
                    cursorColor = TraceoCyan,
                    focusedTextColor = TraceoText,
                    unfocusedTextColor = TraceoText,
                ),
            )
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ServiceFilter.values().forEachIndexed { index, item ->
                    CategoryChip(
                        label = item.label,
                        selected = filterIndex == index,
                        onClick = { onFilterChange(index) },
                    )
                }
            }
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("سرویس‌ها", color = TraceoText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.weight(1f))
                Text("${visibleServices.size} مورد", color = TraceoMuted, fontSize = 11.sp)
            }
        }
        if (visibleServices.isEmpty()) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                EmptyServices()
            }
        } else {
            items(visibleServices, key = { it.id }) { service ->
                ServiceTile(
                    service = service,
                    installed = service.id in installedIds,
                    onClick = { onServiceClick(service) },
                )
            }
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                "برچسب «لینک مستقیم» یعنی می‌توانی بدون ذخیره شماره، چت را باز کنی. بقیه سرویس‌ها فقط برنامه را باز می‌کنند.",
                color = TraceoMuted,
                fontSize = 10.sp,
                lineHeight = 17.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp),
            )
        }
    }
}

@Composable
private fun PhoneSummary(phone: PhoneNumber?, onEditPhone: () -> Unit) {
    val container = if (phone == null) TraceoWarning.copy(alpha = 0.08f) else Color(0xFF10283B)
    val borderColor = if (phone == null) TraceoWarning.copy(alpha = 0.25f) else TraceoCyan.copy(alpha = 0.20f)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(container, RoundedCornerShape(18.dp))
            .border(1.dp, borderColor, RoundedCornerShape(18.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Icon(
            if (phone == null) Icons.Filled.Warning else Icons.Filled.Phone,
            contentDescription = null,
            tint = if (phone == null) TraceoWarning else TraceoCyan,
            modifier = Modifier.size(20.dp),
        )
        Column(Modifier.weight(1f)) {
            Text(
                if (phone == null) "شماره‌ای وارد نشده" else "شماره فعال",
                color = TraceoMuted,
                fontSize = 10.sp,
            )
            Text(
                phone?.formatted ?: "برای استفاده از لینک مستقیم، اول شماره را وارد کن.",
                color = TraceoText,
                fontSize = if (phone == null) 11.sp else 14.sp,
                fontWeight = if (phone == null) FontWeight.Normal else FontWeight.SemiBold,
                maxLines = 2,
            )
        }
        TextButton(onClick = onEditPhone) {
            Text(if (phone == null) "وارد کردن" else "ویرایش", color = TraceoCyan, fontSize = 12.sp)
        }
    }
}

@Composable
private fun CategoryChip(label: String, selected: Boolean, onClick: () -> Unit) {
    val color = if (selected) TraceoCyan else TraceoMuted
    Text(
        label,
        color = color,
        fontSize = 12.sp,
        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
        modifier = Modifier
            .background(
                if (selected) TraceoAccent.copy(alpha = 0.16f) else Color(0xFF11202F),
                RoundedCornerShape(50),
            )
            .border(
                1.dp,
                if (selected) TraceoCyan.copy(alpha = 0.45f) else Color.Transparent,
                RoundedCornerShape(50),
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp),
    )
}

@Composable
private fun EmptyServices() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Icon(Icons.Filled.Search, null, tint = TraceoMuted, modifier = Modifier.size(34.dp))
        Text("سرویسی پیدا نشد", color = TraceoText, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
        Text("عبارت جست‌وجو یا فیلتر را تغییر بده.", color = TraceoMuted, fontSize = 11.sp)
    }
}
