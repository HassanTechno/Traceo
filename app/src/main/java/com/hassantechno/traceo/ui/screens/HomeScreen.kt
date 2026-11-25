package com.hassantechno.traceo.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hassantechno.traceo.data.Service
import com.hassantechno.traceo.phone.PhoneNumber
import com.hassantechno.traceo.ui.components.BrandMark
import com.hassantechno.traceo.ui.components.InfoCard
import com.hassantechno.traceo.ui.components.PopularServiceCard
import com.hassantechno.traceo.ui.components.SectionHeader
import com.hassantechno.traceo.ui.components.StepRow
import com.hassantechno.traceo.ui.components.TraceoBadge
import com.hassantechno.traceo.ui.theme.TraceoAccent
import com.hassantechno.traceo.ui.theme.TraceoCyan
import com.hassantechno.traceo.ui.theme.TraceoDanger
import com.hassantechno.traceo.ui.theme.TraceoMuted
import com.hassantechno.traceo.ui.theme.TraceoOutline
import com.hassantechno.traceo.ui.theme.TraceoSuccess
import com.hassantechno.traceo.ui.theme.TraceoSurface
import com.hassantechno.traceo.ui.theme.TraceoText

@Composable
fun HomeScreen(
    phoneInput: String,
    activePhone: PhoneNumber?,
    error: String?,
    popularServices: List<Service>,
    installedIds: Set<String>,
    onPhoneChange: (String) -> Unit,
    onPaste: () -> Unit,
    onSearch: () -> Unit,
    onServiceClick: (Service) -> Unit,
    onShowAllServices: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(18.dp))
        TopBrandRow()
        Spacer(Modifier.height(26.dp))
        Text(
            "شروع گفتگو، بدون ذخیره شماره",
            color = TraceoText,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "شماره را وارد کن، پیام‌رسان را انتخاب کن و مستقیم به گفتگو برو.",
            color = TraceoMuted,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            lineHeight = 21.sp,
        )
        Spacer(Modifier.height(22.dp))
        PhoneCard(
            phoneInput = phoneInput,
            activePhone = activePhone,
            error = error,
            onPhoneChange = onPhoneChange,
            onPaste = onPaste,
            onSearch = onSearch,
        )
        Spacer(Modifier.height(24.dp))
        SectionHeader(
            title = "سرویس‌های محبوب",
            subtitle = "دسترسی سریع به پیام‌رسان‌ها",
            action = {
                TextButton(onClick = onShowAllServices) {
                    Text("همه", color = TraceoCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            },
        )
        Spacer(Modifier.height(10.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(horizontal = 1.dp),
        ) {
            items(popularServices, key = { it.id }) { service ->
                PopularServiceCard(
                    service = service,
                    installed = service.id in installedIds,
                    onClick = { onServiceClick(service) },
                )
            }
        }
        Spacer(Modifier.height(22.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(22.dp),
            colors = CardDefaults.cardColors(containerColor = TraceoSurface),
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                Text(
                    "چطور کار می‌کند؟",
                    color = TraceoText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                )
                StepRow(1, "شماره را وارد کن", "شماره فقط روی گوشی تو پردازش می‌شود.")
                StepRow(2, "پیام‌رسان را انتخاب کن", "لینک مستقیم یا بازکردن برنامه، بسته به سرویس.")
                StepRow(3, "گفتگو را شروع کن", "بدون ذخیره شماره در مخاطبین یا سرور.")
            }
        }
        Spacer(Modifier.height(16.dp))
        InfoCard(
            icon = Icons.Filled.Lock,
            title = "حریم خصوصی",
            body = "Traceo شماره را ذخیره نمی‌کند و به هیچ سروری نمی‌فرستد. همه‌چیز روی دستگاه تو می‌ماند.",
            accent = TraceoSuccess,
        )
        Spacer(Modifier.height(28.dp))
    }
}

@Composable
private fun TopBrandRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        BrandMark(44.dp)
        Spacer(Modifier.width(10.dp))
        Column(Modifier.weight(1f)) {
            Text("Traceo", color = TraceoText, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("بدون ذخیره شماره", color = TraceoMuted, fontSize = 11.sp)
        }
        TraceoBadge("حریم خصوصی", TraceoSuccess, icon = Icons.Filled.Lock)
    }
}

@Composable
private fun PhoneCard(
    phoneInput: String,
    activePhone: PhoneNumber?,
    error: String?,
    onPhoneChange: (String) -> Unit,
    onPaste: () -> Unit,
    onSearch: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.linearGradient(
                    listOf(
                        TraceoAccent.copy(alpha = 0.38f),
                        TraceoCyan.copy(alpha = 0.18f),
                        Color.Transparent,
                    ),
                ),
                RoundedCornerShape(28.dp),
            )
            .padding(1.dp),
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(27.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0E1D2C)),
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Text(
                    "شماره موبایل",
                    color = TraceoText,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                OutlinedTextField(
                    value = phoneInput,
                    onValueChange = onPhoneChange,
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text("۰۹۱۲ ۳۴۵ ۶۷۸۹", color = TraceoMuted, fontSize = 14.sp) },
                    leadingIcon = { Icon(Icons.Filled.Phone, null, tint = TraceoCyan) },
                    trailingIcon = {
                        if (phoneInput.isNotEmpty()) {
                            IconButton(onClick = { onPhoneChange("") }) {
                                Icon(Icons.Filled.Clear, contentDescription = "پاک کردن", tint = TraceoMuted)
                            }
                        }
                    },
                    isError = error != null,
                    supportingText = {
                        Text(
                            error ?: "برای شماره خارجی، کد کشور را با + وارد کن.",
                            color = if (error != null) TraceoDanger else TraceoMuted,
                            fontSize = 11.sp,
                            lineHeight = 17.sp,
                        )
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Phone,
                        imeAction = ImeAction.Search,
                    ),
                    keyboardActions = KeyboardActions(onSearch = { onSearch() }),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TraceoCyan,
                        unfocusedBorderColor = TraceoOutline,
                        focusedLabelColor = TraceoCyan,
                        cursorColor = TraceoCyan,
                        focusedTextColor = TraceoText,
                        unfocusedTextColor = TraceoText,
                        errorBorderColor = TraceoDanger,
                    ),
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextButton(
                        onClick = onPaste,
                        contentPadding = PaddingValues(horizontal = 4.dp, vertical = 0.dp),
                    ) {
                        Text("چسباندن از کلیپ‌بورد", color = TraceoCyan, fontSize = 12.sp)
                    }
                    Spacer(Modifier.weight(1f))
                    if (activePhone != null) {
                        Text(
                            "فعال: ${activePhone.localFormatted}",
                            color = TraceoSuccess,
                            fontSize = 10.sp,
                            maxLines = 1,
                        )
                    }
                }
                Button(
                    onClick = onSearch,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = TraceoAccent),
                ) {
                    Icon(Icons.Filled.Search, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("پیدا کن", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Filled.Lock, null, tint = TraceoMuted, modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(5.dp))
                    Text(
                        "شماره روی دستگاه می‌ماند و ارسال نمی‌شود.",
                        color = TraceoMuted,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}
